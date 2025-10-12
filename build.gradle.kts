/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

group = "dev.breakin"
version = "1.0-SNAPSHOT"

allprojects {
    group = "dev.breakin"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

// ============================================================
// 프로젝트 모듈 구조
// ============================================================
// - applications: 실행 가능한 애플리케이션 (bootJar enabled)
// - tasks: 배치 작업 단위 모듈
// - common: 범용 기술 통합 모듈 (OpenAI, Prometheus, Logging 등)
// - (root): 프로젝트 고유 비즈니스 로직 모듈
// ============================================================

val javaProjects = listOf(
    // Applications (실행 가능한 애플리케이션)
    project(":modules:applications:api-application"),
    project(":modules:applications:batch-application"),

    // Tasks (배치 작업 모듈)
    project(":modules:tasks:resource-crawl-task"),
    project(":modules:tasks:elasticsearch-sync-task"),

    // Common (범용 기술 모듈)
    project(":modules:common:openai-base"),

    // Business modules (비즈니스 로직)
    project(":modules:model"),
    project(":modules:exception"),
    project(":modules:infrastructure"),
    project(":modules:service"),
    project(":modules:repository-jdbc"),
    project(":modules:api"),
    project(":modules:elasticsearch"),
    project(":modules:auth"),
    project(":modules:outbox"),
    project(":modules:schema")
)

configure(javaProjects) {
    apply(plugin = "java")
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        // Lombok 전역 설정
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

        // 테스트 의존성 - BOM 사용
        testImplementation(platform("org.junit:junit-bom:5.14.0"))
        testImplementation(platform("org.mockito:mockito-bom:5.20.0"))

        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.assertj:assertj-core:3.26.3")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }
}