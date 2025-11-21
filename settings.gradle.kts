enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url =uri("https://jitpack.io") }
    }
}

rootProject.name = "OneRainbow"
include(":app")
include(":lib_base")
include(":lib_net")
include(":lib_route")
include(":lib_database")

include("page")
include("page:mv")
include("page:recommend")
include("page:share")
include("page:search")
include("page:musicplayer")
include("page:top")
include("page:home")
include("page:account")
