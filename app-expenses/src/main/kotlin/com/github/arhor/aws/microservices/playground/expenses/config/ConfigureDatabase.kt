package com.github.arhor.aws.microservices.playground.expenses.config

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Optional
import java.util.function.Supplier

@Configuration(proxyBeanMethods = false)
@EnableJdbcAuditing(modifyOnCreate = false, dateTimeProviderRef = "currentDateTimeProvider")
@EnableJdbcRepositories(basePackages = ["com.github.arhor.aws.microservices.playground.expenses.data.repository"])
@EnableTransactionManagement
class ConfigureDatabase {

    @Bean
    fun currentDateTimeSupplier() = Supplier {
        val systemUTC = Clock.systemUTC()
        val timestamp = ZonedDateTime.now(systemUTC)

        timestamp.truncatedTo(ChronoUnit.MILLIS)
    }

    @Bean
    fun currentDateTimeProvider(currentDateTimeSupplier: Supplier<ZonedDateTime>) = DateTimeProvider {
        Optional.of(currentDateTimeSupplier.get())
    }

    @Bean
    fun flywayConfigurationCustomizer() = FlywayConfigurationCustomizer {
        it.loggers("slf4j")
    }
}
