package com.backend.study

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.backend.study")
class BackendStudyApplication

fun main(args: Array<String>) {
    runApplication<BackendStudyApplication>(*args)
}
