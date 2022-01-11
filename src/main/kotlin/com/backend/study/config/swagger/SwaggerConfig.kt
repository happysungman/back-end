package com.backend.study.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.OAS_30)
            .useDefaultResponseMessages(true)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.backend.study.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(this.apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("코틀린 기반 백엔드 기술 스터디 API")
            .description("java17, kotlin, jpa, r2dbc, webflux 등")
            .version("1.0")
            .build()
    }
}