val benchmarkVersion = providers.gradleProperty("benchmark.version").get()
val coroutinesVersion = providers.gradleProperty("kotlinx.coroutines.version").get()
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.benchmark")
}

repositories {
    mavenCentral()
}

kotlin {
    macosArm64 {
        binaries {
            executable("profile", listOf(org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE)) {
                entryPoint = "main"
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:$benchmarkVersion")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        }
    }
}

benchmark {
    targets {
        register("macosArm64")
    }

    configurations {
        named("main") {
            warmups = 3
            iterations = 5
            iterationTime = 1
            iterationTimeUnit = "s"
            reportFormat = "json"
        }
    }
}

tasks.wrapper {
    gradleVersion = "8.12"
}
