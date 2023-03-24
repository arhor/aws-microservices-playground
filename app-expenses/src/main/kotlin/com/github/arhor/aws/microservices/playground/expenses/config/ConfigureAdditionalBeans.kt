package com.github.arhor.aws.microservices.playground.expenses.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
class ConfigureAdditionalBeans {

    @Bean
    fun currentDateTimeSupplier() = Supplier {
        val systemUTC = Clock.systemUTC()
        val timestamp = ZonedDateTime.now(systemUTC)

        timestamp.truncatedTo(ChronoUnit.MILLIS)
    }
}
