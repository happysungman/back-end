package com.backend.study.config.datasource.r2dbc

import com.backend.study.util.logger
import io.r2dbc.spi.ConnectionFactory
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono

class RoutingConnectionFactory(leader: ConnectionFactory, follower: ConnectionFactory) :
    AbstractRoutingConnectionFactory() {
    override fun determineCurrentLookupKey(): Mono<Any> = TransactionSynchronizationManager.forCurrentTransaction()
        .flatMap {
            logger().info(
                "currentTransactionName: ${it.currentTransactionName}, " +
                        "isActualTransactionActive: ${it.isActualTransactionActive}, " +
                        "isCurrentTransactionReadOnly: ${it.isCurrentTransactionReadOnly}"
            )

            if (it.isActualTransactionActive && it.isCurrentTransactionReadOnly) Mono.just(FOLLOWER)
            else Mono.just(LEADER)
        }
        .doOnSuccess { logger().info("determined key : $it") }
        .doOnError { logger().info("not determined by error : $it") }
        .onErrorReturn(FOLLOWER)

    init {
        setDefaultTargetConnectionFactory(follower)
        setTargetConnectionFactories(mapOf(LEADER to leader, FOLLOWER to follower))
        afterPropertiesSet()
    }

    companion object {
        val LEADER: Any = "leader"
        val FOLLOWER: Any = "follower"
    }
}