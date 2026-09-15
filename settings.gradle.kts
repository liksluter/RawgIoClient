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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RawgIo Client"
include(":app")
include(":core:network:api")
include(":core:network:impl")
include(":core:database")
include(":core:navigation")
include(":core:design-system")
include(":core:media")
include(":core:common")
include(":feature:feed:api")
include(":feature:feed:impl")
include(":feature:details:api")
include(":feature:details:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:settings:api")
include(":feature:settings:impl")
