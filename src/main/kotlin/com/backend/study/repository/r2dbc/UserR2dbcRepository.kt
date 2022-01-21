package com.backend.study.repository.r2dbc

import com.backend.study.repository.r2dbc.entity.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserR2dbcRepository : ReactiveCrudRepository<User, String> {
}
