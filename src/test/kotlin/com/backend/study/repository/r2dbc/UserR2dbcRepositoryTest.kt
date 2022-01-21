package com.backend.study.repository.r2dbc

import com.backend.study.config.datasource.r2dbc.R2dbcBackendDataSourceConfig
import com.backend.study.config.datasource.r2dbc.R2dbcBackendFollowerDataSourceProperties
import com.backend.study.config.datasource.r2dbc.R2dbcBackendLeaderDataSourceProperties
import com.backend.study.repository.r2dbc.entity.User
import com.backend.study.test.testWithTransactional
import com.backend.study.util.logger
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import reactor.kotlin.test.test
import java.time.LocalDate

@SpringBootTest(classes = [R2dbcBackendDataSourceConfig::class])
@EnableConfigurationProperties(
    value = [
        R2dbcBackendLeaderDataSourceProperties::class,
        R2dbcBackendFollowerDataSourceProperties::class
    ]
)
class UserR2dbcRepositoryTest(
    private val transactionManager: R2dbcTransactionManager,
    private val userR2dbcRepository: UserR2dbcRepository
) : StringSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        "R2DBC 레파지토리 Bean 정상 생성 검증" {
            userR2dbcRepository shouldNotBe null
        }

        // 테스트 클래스에 @Transactional 붙여도 R2DBC 트랜잭션 롤백 안되는 이슈가 있음
        "트랜잭션 롤백 정상 동장 검증 : testWithTransactional(transactionManager)" {
            //given
            val user = User(null, "sungman", "성만", LocalDate.of(1992, 11, 8), null)
            val countBeforeSaved = userR2dbcRepository.count().block()
            logger().info("USER count 조회 결과 : $countBeforeSaved")

            //when
            val saved = userR2dbcRepository.save(user)

            //then
            saved.log("유저 저장")
                .zipWhen { userR2dbcRepository.count().log("저장 후 비교할 count") }
                .testWithTransactional(transactionManager)
                .assertNext {
                    it.t1.id shouldNotBe null
                    it.t1.createdAt shouldNotBe null
                    it.t2.shouldBe(countBeforeSaved!! + 1)
                }
                .verifyComplete()

            userR2dbcRepository.count().log("테스트 검증 후 롤백된 이후 count")
                .test()
                .assertNext { it shouldBe countBeforeSaved }
        }
    }
}
