package com.backend.study.config.datasource.jpa

import com.zaxxer.hikari.HikariConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.test.context.SpringBootTest

@ConfigurationPropertiesScan("com.backend.study.config.datasource.jpa")
@SpringBootTest(classes = [BackendDataSourceConfig::class])
class BackendDataSourceTest(
    private val backendLeaderHikariConfig: HikariConfig,
    private val backendFollowerHikariConfig: HikariConfig
) : ShouldSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        context("Spring Context") {
            should("JPA 데이터소스 관리 bean들은 정상 생성되어야 함") {
                backendLeaderHikariConfig shouldNotBe null
                backendFollowerHikariConfig shouldNotBe null

                backendLeaderHikariConfig.isReadOnly shouldBe false
                backendFollowerHikariConfig.isReadOnly shouldBe true
            }
        }
    }
}
