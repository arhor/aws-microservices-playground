package com.github.arhor.aws.microservices.playground.expenses.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration(proxyBeanMethods = false)
class ConfigureRouter {

    @Bean
    fun applicationRouter() = router {
        GET(pattern = "favicon.ico") { ServerResponse.noContent().build() }
    }
}
