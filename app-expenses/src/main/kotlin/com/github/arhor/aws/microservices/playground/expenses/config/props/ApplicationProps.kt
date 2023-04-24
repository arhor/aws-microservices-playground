package com.github.arhor.aws.microservices.playground.expenses.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("application-props")
data class ApplicationProps @ConstructorBinding constructor(
    val apiPathPrefix: String?,
    val aws: Aws,
) {

    data class Aws(
        val expenseUpdatedTopicName: String,
        val expenseDeletedTopicName: String,
    )
}
