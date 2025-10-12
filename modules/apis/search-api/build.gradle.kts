/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":modules:breakin:model"))
    implementation(project(":modules:breakin:elasticsearch"))
    implementation(project(":modules:breakin:infrastructure"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
