package com.backend.study.config.datasource.r2dbc

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource.backend.r2dbc")
class R2dbcBackendProperties {
    var leader: R2dbcProperty = R2dbcProperty()
    var follower: R2dbcProperty = R2dbcProperty()
}

class R2dbcProperty {
    lateinit var host: String
    lateinit var port: String
    lateinit var database: String
    lateinit var username: String
    lateinit var password: String
}