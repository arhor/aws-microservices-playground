plugins {
    id("java-platform")
}

dependencies {
    constraints {
        api("com.amazonaws:aws-lambda-java-core:${project.property("versions.aws-lambda-java-core")}")
        api("com.ninja-squad:springmockk:${project.property("versions.spring-mockk")}")
    }
}

tasks {
    wrapper {
        gradleVersion = project.property("versions.gradle")!!.toString()
    }
}

