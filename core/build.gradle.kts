plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A multiplatform work scheduling library"

kotlin {
    if (Targeting.JVM) jvm { library() }
    if (Targeting.JS) js(IR) { library(testTimeout = 60000) }
    if (Targeting.WASM) wasmJs { library(testTimeout = 60000) }
    val osxTargets = if (Targeting.OSX) osxTargets() else listOf()
//    if (Targeting.NDK) ndkTargets() else listOf()
    if (Targeting.LINUX) linuxTargets() else listOf()
//    if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        commonMain.dependencies {
            api(libs.status.core)
            api(libs.cinematic.live.kollections)
            api(libs.koncurrent.later.coroutines)
        }

        commonTest.dependencies {
            api(libs.koncurrent.later.test)
            api(libs.kommander.coroutines)
        }

        if(Targeting.JVM) jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
        }
    }
}