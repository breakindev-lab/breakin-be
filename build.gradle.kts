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

// 모듈이 생성되면 여기에 추가됩니다
val javaProjects = listOf(
    project(":modules:model"),
    project(":modules:exception"),
    project(":modules:infrastructure"),
    project(":modules:service"),
    project(":modules:repository-jdbc"),
    project(":modules:api"),
    project(":modules:schema"),
    project(":modules:application-api"),
    project(":modules:openai-base"),
    project(":modules:elasticsearch"),
    project(":modules:auth"),

    project(":modules:resource-crawl-task"),

    project(":modules:application-batch"),
    project(":modules:outbox"),
     project(":modules:elasticsearch-sync-task")



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