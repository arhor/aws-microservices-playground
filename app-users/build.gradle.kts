import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management")
    id("org.graalvm.buildtools.native")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
}

val javaVersion = project.property("versions.java")!!.toString()

java {
    javaVersion.let(JavaVersion::toVersion).let {
        sourceCompatibility = it
        targetCompatibility = it
    }
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher.set(
                javaToolchains.launcherFor {
                    languageVersion.set(JavaLanguageVersion.of(javaVersion))
                    vendor.set(JvmVendorSpec.matching("GraalVM Community"))
                }
            )
        }
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.get())
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.code.findbugs:jsr305")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.retry:spring-retry")

    testImplementation("com.ninja-squad:springmockk")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion))
            javaParameters.set(true)
            freeCompilerArgs.set(
                listOf(
                    "-Xjsr305=strict",
                    "-Xjvm-default=all",
                )
            )
        }
    }

    val contractTest by registering(Test::class) {
        group = "verification"
        useJUnitPlatform {
            includeTags("contract")
        }
        shouldRunAfter(test)
    }

    val integrationTest by registering(Test::class) {
        group = "verification"
        useJUnitPlatform {
            includeTags("integration")
        }
        shouldRunAfter(contractTest)
    }

    test {
        useJUnitPlatform {
            excludeTags("contract", "integration")
        }
    }

    check {
        dependsOn(
            test,
            contractTest,
            integrationTest,
        )
    }
}
