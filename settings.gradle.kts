pluginManagement {
    plugins {
        fun prop(name: String): String = settings.extra[name].toString()

        // @formatter:off
        id("org.jetbrains.kotlin.jvm")           version prop("versions.kotlin")
        id("org.jetbrains.kotlin.kapt")          version prop("versions.kotlin")
        id("org.jetbrains.kotlin.plugin.spring") version prop("versions.kotlin")
        id("io.spring.dependency-management")    version prop("versions.springDependencyManagement")
        id("org.springframework.boot")           version prop("versions.springBoot")
        // @formatter:on
    }
}

rootProject.name = "aws-microservices-playground"
