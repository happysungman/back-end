package com.backend.study.controller

import com.backend.study.repository.jpa.UserRepository
import com.backend.study.repository.jpa.entity.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@Tag(name = "회원 컨트롤러")
@RestController
@RequestMapping("/user")
class UserController(val userRepository: UserRepository) {

    @Operation(summary = "회원 조회", tags = ["회원 컨트롤러"])
    @GetMapping
    fun findAllUsers(): List<User> = userRepository.findAll()

    @Operation(summary = "회원 등록", tags = ["회원 컨트롤러"])
    @PostMapping
    fun createUser(@RequestBody user: User): User {
        return userRepository.save(user);
    }
}