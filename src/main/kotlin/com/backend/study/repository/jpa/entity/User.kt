package com.backend.study.repository.jpa.entity

import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Int,

    var name: String,

    var birthday: LocalDate,

    @CreatedDate
    var createdAt: LocalDateTime
)
