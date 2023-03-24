package com.github.arhor.aws.microservices.playground.expenses.config

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
        configurer.addPathPrefix(applicationProps.apiPathPrefix, classesAnnotatedWith<RestController>())
    }

    companion object {
        private inline fun <reified T : Annotation> classesAnnotatedWith() = forAnnotation(T::class.java)
    }
}

