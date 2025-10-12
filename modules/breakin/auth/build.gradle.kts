/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // User 조회를 위한 의존성
    implementation(project(":modules:breakin:model"))
    implementation(project(":modules:breakin:infrastructure"))

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Logging
    implementation("org.slf4j:slf4j-api")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
