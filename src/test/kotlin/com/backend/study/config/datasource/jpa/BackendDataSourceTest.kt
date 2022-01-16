package com.backend.study.config.datasource.jpa

import com.ninjasquad.springmockk.MockkBean
import com.zaxxer.hikari.HikariConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import javax.persistence.EntityManager

@EnableConfigurationProperties
@SpringBootTest(classes = [BackendDataSourceConfig::class])
class BackendDataSourceTest : ShouldSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    private lateinit var backendLeaderHikariConfig: HikariConfig
    private lateinit var backendFollowerHikariConfig: HikariConfig

    private lateinit var entityManager: TestEntityManager;

    init {
        context("Spring Context") {
            should("JPA 데이터소스 관리 bean들은 정상 생성되어야 함") {
                backendLeaderHikariConfig shouldNotBe null
                backendFollowerHikariConfig shouldNotBe null
            }
        }
    }
}
