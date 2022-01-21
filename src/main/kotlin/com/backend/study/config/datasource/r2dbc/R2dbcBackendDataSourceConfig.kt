package com.backend.study.config.datasource.r2dbc

import io.r2dbc.pool.PoolingConnectionFactoryProvider
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.mariadb.r2dbc.MariadbConnectionConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.connection.TransactionAwareConnectionFactoryProxy
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Duration

@EnableR2dbcAuditing
@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = ["com.backend.study.repository.r2dbc"])
class R2dbcBackendDataSourceConfig(
    val leaderDatasourceProperties: R2dbcBackendLeaderDataSourceProperties,
    val followerDatasourceProperties: R2dbcBackendFollowerDataSourceProperties
) : AbstractR2dbcConfiguration() {

    @Bean
    fun leaderConnectionFactory(): ConnectionFactory =
        createPoolingConnectionFactory(leaderDatasourceProperties)

    @Bean
    fun followerConnectionFactory(): ConnectionFactory =
        createPoolingConnectionFactory(followerDatasourceProperties)

    @Bean
    override fun connectionFactory(): ConnectionFactory =
        TransactionAwareConnectionFactoryProxy(
            RoutingConnectionFactory(
                leaderConnectionFactory(),
                followerConnectionFactory()
            )
        )

    @Bean
    fun leaderTransactionManager(@Qualifier("leaderConnectionFactory") connectionFactory: ConnectionFactory):
            R2dbcTransactionManager = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun followerTransactionManager(@Qualifier("followerConnectionFactory") connectionFactory: ConnectionFactory):
            R2dbcTransactionManager = R2dbcTransactionManager(connectionFactory).apply { isEnforceReadOnly = true }

    @Primary
    @Bean
    fun routingTransactionManager(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory):
            R2dbcTransactionManager = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun r2dbcEntityTemplate(@Qualifier("connectionFactory") connectionFactory: ConnectionFactory): R2dbcEntityTemplate =
        R2dbcEntityTemplate(DatabaseClient.create(connectionFactory), MySqlDialect.INSTANCE)

    private fun mariadbConnectionConfiguration(dataSourceProperties: AbstarctDataSourceProperties) =
        MariadbConnectionConfiguration.builder()
            .host(dataSourceProperties.host)
            .port(dataSourceProperties.port)
            .database(dataSourceProperties.database)
            .username(dataSourceProperties.username)
            .password(dataSourceProperties.password)
            .build()

    private fun createPoolingConnectionFactory(dataSourceProperties: AbstarctDataSourceProperties): ConnectionFactory =
        ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, PoolingConnectionFactoryProvider.POOLING_DRIVER)
                .option(ConnectionFactoryOptions.PROTOCOL, dataSourceProperties.protocol)
                .option(ConnectionFactoryOptions.HOST, dataSourceProperties.host)
                .option(ConnectionFactoryOptions.PORT, dataSourceProperties.port)
                .option(ConnectionFactoryOptions.DATABASE, dataSourceProperties.database)
                .option(ConnectionFactoryOptions.USER, dataSourceProperties.username)
                .option(ConnectionFactoryOptions.PASSWORD, dataSourceProperties.password)
                .option(PoolingConnectionFactoryProvider.POOL_NAME, dataSourceProperties.pool.name)
                .option(PoolingConnectionFactoryProvider.INITIAL_SIZE, dataSourceProperties.pool.initialSize)
                .option(PoolingConnectionFactoryProvider.MAX_SIZE, dataSourceProperties.pool.maxSize)
                .option(
                    PoolingConnectionFactoryProvider.MAX_IDLE_TIME,
                    Duration.ofSeconds(dataSourceProperties.pool.maxIdleTime)
                )
                .option(PoolingConnectionFactoryProvider.VALIDATION_QUERY, dataSourceProperties.pool.validationQuery)
                .build()
        )
}
