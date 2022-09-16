import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10"
    kotlin("plugin.allopen") version "1.7.10"
    kotlin("plugin.noarg") version "1.7.10"
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
}

group = "org.gav.api"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Junit 5
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // Persistence
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("javax.validation:validation-api:2.0.1.Final")

    // Database
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    // Serialization/Deserialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Web Framework
    implementation("io.javalin:javalin-bundle:4.6.4")
}

allOpen {
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}
