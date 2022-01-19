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
            if (it.isActualTransactionActive && it.isCurrentTransactionReadOnly) Mono.just(FOLLOWER) else Mono.just(
                LEADER
            )
        }
        .doOnSuccess { logger().info("determined key : $it") }
        .onErrorReturn(LEADER)

    init {
        setDefaultTargetConnectionFactory(leader)
        setTargetConnectionFactories(mapOf(LEADER to leader, FOLLOWER to follower))
    }

    companion object {
        val LEADER: Any = "leader"
        val FOLLOWER: Any = "follower"
    }
}