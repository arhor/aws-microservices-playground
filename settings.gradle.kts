pluginManagement {
    plugins {
        fun prop(name: String): String = extra[name].toString()

        // @formatter:off
        id("com.github.johnrengelman.shadow")    version prop("versions.gradle-shadow-plugin")
        id("org.jetbrains.kotlin.jvm")           version prop("versions.kotlin")
        id("org.jetbrains.kotlin.kapt")          version prop("versions.kotlin")
        id("org.jetbrains.kotlin.plugin.spring") version prop("versions.kotlin")
        id("org.springframework.boot")           version prop("versions.spring-boot")
        id("io.spring.dependency-management")    version prop("versions.spring-dependency-management")
        // @formatter:on
    }
}

rootProject.name = "aws-microservices-playground"

include("app-budget-overrun-tracker")
include("app-email-notifier")
include("app-expenses")
include("app-users")
