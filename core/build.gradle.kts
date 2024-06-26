plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

description = "A multiplatform work scheduling library"

kotlin {
    jvm { library() }
    if (Targeting.JS) js(IR) { library() }
//    if (Targeting.WASM) wasm { library() }
    val osxTargets = if (Targeting.OSX) osxTargets() else listOf()
//    val ndkTargets = if (Targeting.NDK) ndkTargets() else listOf()
    val linuxTargets = if (Targeting.LINUX) linuxTargets() else listOf()
//    val mingwTargets = if (Targeting.MINGW) mingwTargets() else listOf()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.keep.api)
                api(libs.kase.core)
                api(libs.status.core)
                api(libs.cinematic.live.kollections)
                api(libs.koncurrent.later.coroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                api(libs.koncurrent.later.test)
                api(libs.kommander.coroutines)
            }
        }
    }
}