
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    // Common modules
    implementation(project(":modules:common:logging"))

    // Batch modules
    implementation(project(":modules:tasks:resource-crawl-task"))
    implementation(project(":modules:tasks:elasticsearch-sync-task"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    // Database
    runtimeOnly("com.h2database:h2")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// application-batch는 실행 가능한 JAR
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}

tasks.named<Jar>("jar") {
    enabled = false
}
