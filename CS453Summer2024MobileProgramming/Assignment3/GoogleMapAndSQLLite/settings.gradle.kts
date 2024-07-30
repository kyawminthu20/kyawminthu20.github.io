pluginManagement {
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
    plugins {
        id("com.android.application") version "8.0.0"
        id("org.jetbrains.kotlin.android") version "1.8.21"
        id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "1.3.0"
        //alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GoogleMapAndSQLLite"
include(":app")
