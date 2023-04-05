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
    annotationProcessor(platform(rootProject))
    annotationProcessor("com.google.dagger:dagger-compiler")

    implementation(platform(rootProject))
    implementation("com.amazonaws:aws-java-sdk-ses")
    implementation("com.amazonaws:aws-java-sdk-sqs")
    implementation("com.amazonaws:aws-lambda-java-core")
    implementation("com.amazonaws:aws-lambda-java-events")
    implementation("org.apache.commons:commons-text")
    implementation("com.google.dagger:dagger")

    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")

    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2")

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

    assemble {
        dependsOn(shadowJar)
    }
}
