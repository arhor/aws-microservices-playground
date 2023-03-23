package com.github.arhor.aws.microservices.playground.users.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("application-props")
data class ApplicationProps @ConstructorBinding constructor(
    val apiPathPrefix: String?,
    var retryAttempts: Int,
) {

    fun apiUrlPath(url: String): String {
        val prefix = apiPathPrefix ?: ""
        val suffix = if (url.startsWith("/")) url.drop(1) else url

        return prefix + suffix
    }
}
