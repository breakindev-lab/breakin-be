/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

rootProject.name = "breakin-be"

// Applications (실행 가능한 애플리케이션)
include(":modules:applications:api-application")
include(":modules:applications:batch-application")

// Tasks (배치 작업 모듈)
include(":modules:tasks:resource-crawl-task")
include(":modules:tasks:elasticsearch-sync-task")

// Common (범용 기술 모듈)
include(":modules:common:openai-base")

// Business modules (비즈니스 로직 모듈)
include(":modules:model")
include(":modules:exception")
include(":modules:infrastructure")
include(":modules:service")
include(":modules:repository-jdbc")
include(":modules:api")
include(":modules:elasticsearch")
include(":modules:auth")
include(":modules:outbox")
include(":modules:schema")
pluginManagement {
    buildscript {
        repositories {
            gradlePluginPortal()
        }
    }

    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}