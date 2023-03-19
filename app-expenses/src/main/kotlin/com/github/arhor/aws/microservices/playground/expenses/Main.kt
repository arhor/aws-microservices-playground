package com.github.arhor.aws.microservices.playground.expenses

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
