/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    // Spring Core
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-context")

    // Database
    runtimeOnly("com.h2database:h2")

    // Project modules
    implementation(project(":modules:model"))
    implementation(project(":modules:exception"))
    implementation(project(":modules:outbox"))
    implementation(project(":modules:elasticsearch"))
    implementation(project(":modules:infrastructure"))

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
