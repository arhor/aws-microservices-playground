plugins {
    id("com.github.johnrengelman.shadow")
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(platform(rootProject))
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("com.google.dagger:dagger-compiler")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2")

    implementation(platform(rootProject))
    implementation("com.amazonaws:aws-java-sdk-dynamodb")
    implementation("com.amazonaws:aws-java-sdk-sns")
    implementation("com.amazonaws:aws-lambda-java-core")
    implementation("com.amazonaws:aws-lambda-java-events")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.google.dagger:dagger")
    implementation("org.apache.logging.log4j:log4j-api")
    implementation("org.apache.logging.log4j:log4j-core")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl")

    testAnnotationProcessor(platform(rootProject))
    testAnnotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.assertj:assertj-core")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
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
