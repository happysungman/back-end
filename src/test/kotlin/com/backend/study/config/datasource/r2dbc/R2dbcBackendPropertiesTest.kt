package com.backend.study.config.datasource.r2dbc

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(R2dbcBackendProperties::class)
class R2dbcBackendPropertiesTest(private val properties: R2dbcBackendProperties) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "스프링 컨텍스트에서 R2dbcBackendProperties 정상 로드 검증" {
            properties shouldNotBe null
            properties.leader shouldNotBe null
            properties.leader.port shouldBe 3307

            properties.follower shouldNotBe null
            properties.follower.port shouldBe 3307
        }
    }
}
