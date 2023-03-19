plugins {
    id("com.github.johnrengelman.shadow")
    id("java")
}

java {
    project.property("versions.java")!!.let(JavaVersion::toVersion).let {
        sourceCompatibility = it
        targetCompatibility = it
    }
}

repositories {
    mavenCentral()
}

configurations {
    implementation {
        exclude(module = "apache-client")
        exclude(module = "commons-logging")
        exclude(module = "netty-nio-client")
    }
}

dependencies {
    annotationProcessor(platform(rootProject))
    annotationProcessor("com.google.dagger:dagger-compiler")

    implementation(platform(rootProject))
    implementation("com.amazonaws:aws-java-sdk-dynamodb")
    implementation("com.amazonaws:aws-java-sdk-sns")
    implementation("com.amazonaws:aws-lambda-java-core")
    implementation("com.amazonaws:aws-lambda-java-events")
    implementation("com.google.dagger:dagger")

    testImplementation("com.amazonaws:aws-lambda-java-tests")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-api")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    withType<JavaCompile> {
        options.compilerArgs = listOf(
            "-Xlint:deprecation",
            "-Xlint:fallthrough",
            "-Xlint:unchecked",
            "-parameters",
        )
    }

    withType<Test> {
        useJUnitPlatform()
    }

    build {
        finalizedBy(shadowJar)
    }
}
