package com.github.arhor.aws.microservices.playground.users.config

import com.github.arhor.aws.microservices.playground.users.config.props.ApplicationProps
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerTypePredicate.forAnnotation
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ApplicationProps::class)
class ConfigureWebServer(private val appProps: ApplicationProps) : WebMvcConfigurer {

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        appProps.apiPathPrefix?.let { configurer.addPathPrefix(it, classesAnnotatedWith<RestController>()) }
    }

    companion object {
        private inline fun <reified T : Annotation> classesAnnotatedWith() = forAnnotation(T::class.java)
    }
}

