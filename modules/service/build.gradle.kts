/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":modules:model"))
    implementation(project(":modules:exception"))
    implementation(project(":modules:infrastructure"))
    implementation(project(":modules:auth"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-tx")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
