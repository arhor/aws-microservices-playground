package com.github.arhor.aws.microservices.playground.expenses.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("application-props")
data class ApplicationProps @ConstructorBinding constructor(
    val apiPathPrefix: String,
    val retryAttempts: Int,
)