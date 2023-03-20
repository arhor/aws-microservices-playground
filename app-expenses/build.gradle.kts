plugins {
    id("io.spring.dependency-management")
    id("org.springframework.boot")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.spring")
}

java {
    project.property("versions.java")!!.let(JavaVersion::toVersion).let {
        sourceCompatibility = it
        targetCompatibility = it
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
    }
    implementation {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    testImplementation {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito", module = "mockito-core")
        exclude(group = "org.mockito", module = "mockito-junit-jupiter")
    }
    all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(group = "org.apache.logging.log4j", module = "log4j-api")
    }
}

dependencies {
    kapt(platform(rootProject))
    kapt("org.mapstruct:mapstruct-processor")
    kapt("org.springframework:spring-context-indexer")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.mapstruct:mapstruct")

    runtimeOnly("org.postgresql:postgresql")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation(platform(rootProject))
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.retry:spring-retry")

    testImplementation("com.ninja-squad:springmockk")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${project.property("versions.testcontainers")}")
    }
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

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xjsr305=strict",
                "-Xjvm-default=all",
            )
            jvmTarget = "17"
            javaParameters = true
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }
}
