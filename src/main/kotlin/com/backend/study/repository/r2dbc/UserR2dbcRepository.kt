package com.backend.study.repository.r2dbc

import com.backend.study.repository.r2dbc.entity.User
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface UserR2dbcRepository : R2dbcRepository<User, String>
