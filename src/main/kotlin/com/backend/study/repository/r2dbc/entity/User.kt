package com.backend.study.repository.r2dbc.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("user")
data class User(
    @Id
    var userId: Int,

    var name: String,

    var birthday: LocalDate,

    @CreatedDate
    var createdAt: LocalDateTime
)
