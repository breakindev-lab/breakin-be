/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    // Project modules
    implementation(project(":modules:breakin:model"))
    implementation(project(":modules:breakin:exception"))
    //implementation(project(":modules:breakin:infrastructure"))

    // Spring Core
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-context")

    // Spring Data JDBC
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    // Database
    runtimeOnly("com.h2database:h2")

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
