package com.backend.study.repository.jpa

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@Sql(value = ["member_repository_test.sql"])
@DataJpaTest
class MemberRepositoryTest(@Autowired private val memberRepository: MemberRepository) : BehaviorSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    init {
        given("멤버ID가 주어졌을 때") {
            `when`("JPA 레포지토리를 통해 조회하면") {
                then("해당 멤버가 조회된다") {
                    val member = memberRepository.findById(1).get()

                    member shouldNotBe null
                    member.id shouldBe 1
                    member.name shouldBe "김성만"

                    member.team shouldNotBe null
                    member.team.id shouldBe 1
                    member.team.name shouldBe "백엔드팀"
                    member.team.members shouldNotBe null
                    member.team.members.size shouldBe 1
                    member.team.members shouldContain member
                }
            }
        }
    }
}
