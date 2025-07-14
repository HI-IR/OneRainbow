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
include(":module_home")
include(":module_account")
include(":module_recommend")
include(":module_top")
include(":module_mv")
include(":module_user")
include(":lib_route")
include(":lib_database")
include(":module_share")
