/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":modules:breakin:model"))
    implementation(project(":modules:breakin:exception"))
    implementation(project(":modules:breakin:infrastructure"))
    implementation(project(":modules:breakin:auth"))
    implementation(project(":modules:breakin:outbox"))

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
