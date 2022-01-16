package com.backend.study.repository.jpa

import com.backend.study.repository.jpa.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
}