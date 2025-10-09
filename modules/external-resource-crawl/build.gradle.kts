
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    // Spring Core
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework:spring-context")

    // Spring Data JDBC
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

    // Database
    runtimeOnly("com.h2database:h2")

    // Project modules
    implementation(project(":modules:openai-base"))
    implementation(project(":modules:model"))
    implementation(project(":modules:infrastructure"))
    implementation(project(":modules:repository-jdbc"))

    // Firecrawl (예시 - 실제 dependency 확인 필요)
    // implementation("com.firecrawl:firecrawl-java:x.x.x")

    // Playwright
    implementation("com.microsoft.playwright:playwright:1.48.0")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
