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

dependencies {
    implementation(platform(rootProject))
    implementation("com.amazonaws:aws-java-sdk-sqs")
    implementation("com.amazonaws:aws-lambda-java-core")
    implementation("com.amazonaws:aws-lambda-java-events")

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
}
