plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ie.tcd.scss.gpsd"
version = "0.0.12"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

extra["springCloudVersion"] = "2024.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")

    // Test Containers
//    testImplementation("org.testcontainers:postgresql:1.20.4")
//    testImplementation("org.testcontainers:junit-jupiter:1.20.4")

    // Jacoco
//    testImplementation("org.jacoco:core")

    // Database / Postgres related
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.7.3")

    // Lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Mapstruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Springdoc OpenAPI for Spring Boot 3
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")

    // Vault
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")

    // Reactive Spring WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")

//    implementation("io.netty:netty-resolver-dns-native-macos:4.1.117.Final:osx-x86_64")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}



