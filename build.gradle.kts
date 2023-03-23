plugins {
    id("java-platform")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("com.amazonaws:aws-java-sdk-bom:${property("versions.aws-java-sdk")}"))
    api(platform("org.testcontainers:testcontainers-bom:${project.property("versions.testcontainers")}"))

    constraints {
        api("com.amazonaws:aws-lambda-java-core:${property("versions.aws-lambda-java-core")}")
        api("com.amazonaws:aws-lambda-java-events:${property("versions.aws-lambda-java-events")}")
        api("com.amazonaws:aws-lambda-java-tests:${property("versions.aws-lambda-java-tests")}")
        api("com.google.code.findbugs:jsr305:${property("versions.findbugs-jsr")}")
        api("com.google.dagger:dagger:${property("versions.dagger")}")
        api("com.google.dagger:dagger-compiler:${property("versions.dagger")}")
        api("com.ninja-squad:springmockk:${property("versions.spring-mockk")}")
        api("org.assertj:assertj-core:${property("versions.assertj")}")
        api("org.junit.jupiter:junit-jupiter-params:${property("versions.junit-jupiter")}")
        api("org.junit.jupiter:junit-jupiter-api:${property("versions.junit-jupiter")}")
        api("org.junit.jupiter:junit-jupiter-engine:${property("versions.junit-jupiter")}")
        api("org.mapstruct:mapstruct:${property("versions.mapstruct")}")
        api("org.mapstruct:mapstruct-processor:${property("versions.mapstruct")}")
        api("org.mockito:mockito-core:${property("versions.mockito")}")
        api("org.mockito:mockito-junit-jupiter:${property("versions.mockito")}")
        api("org.postgresql:postgresql:${property("versions.postgresql")}")
    }
}

tasks {
    wrapper {
        gradleVersion = project.property("versions.gradle")!!.toString()
    }
}
