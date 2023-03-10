plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    jvm { library() }
    js(IR) { library() }
    val nativeTargets = linuxTargets(true)

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.cacheApi)
                api(projects.kaseCore)
                api(projects.presentersStates)
                api(projects.liveKollections)
                api(projects.koncurrentLaterCoroutines)
            }
        }

        val commonTest by getting {
            dependencies {
                api(projects.koncurrentLaterTest)
                api(projects.expectCoroutines)
            }
        }
    }
}