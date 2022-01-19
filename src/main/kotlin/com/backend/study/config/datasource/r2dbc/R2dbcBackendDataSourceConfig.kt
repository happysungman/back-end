package com.backend.study.config.datasource.r2dbc

import io.r2dbc.spi.ConnectionFactory
import org.mariadb.r2dbc.MariadbConnectionConfiguration
import org.mariadb.r2dbc.MariadbConnectionFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy
import org.springframework.transaction.ReactiveTransactionManager

@Configuration
@EnableR2dbcRepositories(basePackages = ["com.backend.study.repository.r2dbc"])
class R2dbcBackendDataSourceConfig(val r2dbcBackendProperties: R2dbcBackendProperties) : AbstractR2dbcConfiguration() {

    @Bean
    fun leaderConnectionFactory(): ConnectionFactory =
        MariadbConnectionFactory(mariadbConnectionConfiguration(r2dbcBackendProperties.leader))

    @Bean
    fun followerConnectionFactory(): ConnectionFactory =
        MariadbConnectionFactory(mariadbConnectionConfiguration(r2dbcBackendProperties.follower))


    @Bean
    override fun connectionFactory(): ConnectionFactory =
        RoutingConnectionFactory(leaderConnectionFactory(), followerConnectionFactory())

    @Bean
    fun leaderTransactionManager(@Qualifier("leaderConnectionFactory") connectionFactory: ConnectionFactory):
            ReactiveTransactionManager = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun followerTransactionManager(@Qualifier("followerConnectionFactory") connectionFactory: ConnectionFactory):
            ReactiveTransactionManager = R2dbcTransactionManager(connectionFactory).apply { isEnforceReadOnly = true }

    @Bean
    fun routingTransactionManager(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory):
            ReactiveTransactionManager =
        R2dbcTransactionManager(TransactionAwareConnectionFactoryProxy(connectionFactory))


    private fun mariadbConnectionConfiguration(r2dbcProperty: R2dbcProperty) = MariadbConnectionConfiguration.builder()
        .host(r2dbcProperty.host)
        .port(r2dbcProperty.port.toInt())
        .database(r2dbcProperty.database)
        .username(r2dbcProperty.username)
        .password(r2dbcProperty.password)
        .build();


}