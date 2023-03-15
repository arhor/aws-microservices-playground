plugins {
    id("java-platform")
}

dependencies {
    constraints {
        // declare version constraints here
    }
}

tasks {
    wrapper {
        gradleVersion = project.property("versions.gradle")!!.toString()
    }
}

