package com.github.arhor.aws.microservices.playground.users

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.context.WebServerApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.retry.annotation.EnableRetry
import java.lang.invoke.MethodHandles

private val log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

@EnableRetry
@SpringBootApplication(proxyBeanMethods = false)
class Main {

    @Bean
    @Profile("dev")
    fun displayApplicationInfo(context: WebServerApplicationContext) = ApplicationRunner {
        val port = context.webServer.port
        val path = context.environment.getProperty("spring.webflux.base-path", "")

        log.info("Local access URL: http://localhost:{}{}", port, path)
    }
}

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
