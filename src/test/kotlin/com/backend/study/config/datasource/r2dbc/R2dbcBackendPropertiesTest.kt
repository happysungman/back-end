package com.backend.study.config.datasource.r2dbc

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [R2dbcBackendDataSourceConfig::class])
@EnableConfigurationProperties(
    value = [
        R2dbcBackendLeaderDataSourceProperties::class, R2dbcBackendFollowerDataSourceProperties::class]
)
class R2dbcBackendPropertiesTest(
    private val leaderDatasourceProperties: R2dbcBackendLeaderDataSourceProperties,
    private val followerDatasourceProperties: R2dbcBackendFollowerDataSourceProperties
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "스프링 컨텍스트에서 생성된 DataSourceProperties 정상 로드 검증" {
            leaderDatasourceProperties shouldNotBe null
            leaderDatasourceProperties.port shouldBe 3307
            leaderDatasourceProperties.pool.validationQuery shouldBe "SELECT 1"

            followerDatasourceProperties shouldNotBe null
            followerDatasourceProperties.port shouldBe 3307
            followerDatasourceProperties.pool.validationQuery shouldBe "SELECT 1"
        }
    }
}
