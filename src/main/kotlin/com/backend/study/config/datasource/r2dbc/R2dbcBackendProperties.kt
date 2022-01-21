package com.backend.study.config.datasource.r2dbc

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

abstract class AbstarctDataSourceProperties(
    open val protocol: String,
    open val host: String,
    open val port: Int,
    open val database: String,
    open val username: String,
    open val password: String,
    open val pool: PoolProperties
) {
    data class PoolProperties(
        val name: String, val
        initialSize: Int, val
        maxSize: Int, val
        maxIdleTime: Long, val
        validationQuery: String
    )
}

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource.backend.r2dbc.leader")
data class R2dbcBackendLeaderDataSourceProperties(
    override val protocol: String,
    override val host: String,
    override val port: Int,
    override val database: String,
    override val username: String,
    override val password: String,
    override val pool: PoolProperties
) : AbstarctDataSourceProperties(protocol, host, port, database, username, password, pool)

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource.backend.r2dbc.follower")
data class R2dbcBackendFollowerDataSourceProperties(
    override val protocol: String,
    override val host: String,
    override val port: Int,
    override val database: String,
    override val username: String,
    override val password: String,
    override val pool: PoolProperties
) : AbstarctDataSourceProperties(protocol, host, port, database, username, password, pool)
