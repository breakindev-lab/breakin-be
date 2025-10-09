/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

rootProject.name = "breakin-be"

// 향후 모듈들이 여기에 추가됩니다
include(":modules:model")
include(":modules:exception")
include(":modules:infrastructure")
include(":modules:service")
include(":modules:repository-jdbc")
include(":modules:api")
include(":modules:schema")
include(":modules:application-api")
include(":modules:application-batch")
include(":modules:openai-base")
include(":modules:elasticsearch")
include(":modules:auth")
include(":modules:external-resource-crawl")
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
include("modules:openai-base")
include("modules:elasticsearch")