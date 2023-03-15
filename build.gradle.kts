plugins {
    id("java-platform")
}

dependencies {
    constraints {
        api("com.ninja-squad:springmockk:${project.property("versions.springMockk")}")
    }
}

tasks {
    wrapper {
        gradleVersion = project.property("versions.gradle")!!.toString()
    }
}

