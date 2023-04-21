package com.github.arhor.aws.microservices.playground.users.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "application-props")
data class ApplicationProps @ConstructorBinding constructor(
    val apiPathPrefix: String?,
    val aws: Aws,
) {

    data class Aws(
        val userUpdatedTopicName: String,
        val userDeletedTopicName: String,
    )
}
