pluginManagement {
    val kotlinVersion = providers.gradleProperty("kotlin.version").get()
    val benchmarkVersion = providers.gradleProperty("benchmark.version").get()

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.multiplatform" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlinx.benchmark" -> useVersion(benchmarkVersion)
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "exception-regression"
