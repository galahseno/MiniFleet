pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

//gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MiniFleet"
include(":app")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":core:presentation:design_system")
include(":core:presentation:ui")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":maps:data")
include(":maps:domain")
include(":maps:presentation")
include(":dashboard:data")
include(":dashboard:domain")
include(":dashboard:presentation")
include(":alerts:data")
include(":alerts:domain")
include(":alerts:presentation")
