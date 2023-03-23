package com.github.arhor.aws.microservices.playground.users.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerTypePredicate.forAnnotation
import org.springframework.web.reactive.config.PathMatchConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ApplicationProps::class)
class ConfigureWebServer(private val applicationProps: ApplicationProps) : WebFluxConfigurer {

    override fun configurePathMatching(configurer: PathMatchConfigurer) {
        applicationProps.apiPathPrefix?.let {
            configurer.addPathPrefix(it, annotatedWith<RestController>())
        }
    }

    companion object {
        private inline fun <reified T : Annotation> annotatedWith() = forAnnotation(T::class.java)
    }
}

