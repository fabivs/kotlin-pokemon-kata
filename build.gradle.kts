val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id("com.ncorti.ktfmt.gradle") version "0.18.0"
}

group = "com.pokedex"
version = "0.0.1"

application {
    mainClass.set("com.pokedex.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}

ktfmt {
    kotlinLangStyle()
}