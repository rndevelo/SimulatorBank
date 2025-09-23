pluginManagement {

    val gprUser = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GPR_USER")
    val gprKey = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GPR_KEY")

    println("GPR_USER = $gprUser")
    println("GPR_KEY present? = ${gprKey != null}")

    repositories {
        mavenCentral() // MOVIDO AL PRINCIPIO
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/rndevelo/build-logic")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull
                    ?: System.getenv("GPR_USER")
                password = providers.gradleProperty("gpr.key").orNull
                    ?: System.getenv("GPR_KEY")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SimulatorBank"
include(":app")
include(":features:auth:auth-domain")
include(":features:auth:auth-data")
include(":features:auth:auth-presentation")
include(":features:account:account-domain")
include(":features:account:account-data")
include(":features:account:account-presentation")
