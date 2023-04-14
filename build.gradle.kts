plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("com.amazonaws:aws-java-sdk-bom:${property("versions.aws-java-sdk")}"))
    api(platform("org.testcontainers:testcontainers-bom:${project.property("versions.testcontainers")}"))

    constraints {
        api("com.amazonaws:amazon-sqs-java-messaging-lib:${property("versions.aws-java-sqs-jms")}")
        api("com.amazonaws:aws-lambda-java-core:${property("versions.aws-java-lambda-core")}")
        api("com.amazonaws:aws-lambda-java-events:${property("versions.aws-java-lambda-events")}")
        api("com.amazonaws:aws-lambda-java-log4j2:${property("versions.aws-java-lambda-log4j2")}")
        api("com.amazonaws:aws-lambda-java-tests:${property("versions.aws-java-lambda-tests")}")
        api("com.google.code.findbugs:jsr305:${property("versions.findbugs-jsr")}")
        api("com.google.dagger:dagger:${property("versions.dagger")}")
        api("com.google.dagger:dagger-compiler:${property("versions.dagger")}")
        api("com.ninja-squad:springmockk:${property("versions.spring-mockk")}")
        api("io.awspring.cloud:spring-cloud-starter-aws-messaging:${property("versions.spring-cloud-aws")}")
        api("org.apache.commons:commons-text:${property("versions.commons-text")}")
        api("org.apache.logging.log4j:log4j-api:${property("versions.log4j2")}")
        api("org.apache.logging.log4j:log4j-core:${property("versions.log4j2")}")
        api("org.apache.logging.log4j:log4j-slf4j2-impl:${property("versions.log4j2")}")
        api("org.assertj:assertj-core:${property("versions.assertj")}")
        api("org.junit.jupiter:junit-jupiter-params:${property("versions.junit-jupiter")}")
        api("org.junit.jupiter:junit-jupiter-api:${property("versions.junit-jupiter")}")
        api("org.junit.jupiter:junit-jupiter-engine:${property("versions.junit-jupiter")}")
        api("org.mapstruct:mapstruct:${property("versions.mapstruct")}")
        api("org.mapstruct:mapstruct-processor:${property("versions.mapstruct")}")
        api("org.mockito:mockito-core:${property("versions.mockito")}")
        api("org.mockito:mockito-junit-jupiter:${property("versions.mockito")}")
        api("org.postgresql:postgresql:${property("versions.postgresql")}")
        api("org.projectlombok:lombok:${property("versions.lombok")}")
    }
}

tasks {
    wrapper {
        gradleVersion = project.property("versions.gradle")!!.toString()
    }
}
