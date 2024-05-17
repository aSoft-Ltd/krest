import java.io.File

pluginManagement {
    includeBuild("../build-logic")
}

plugins {
    id("multimodule")
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

listOf(
    "cinematic", "keep", "lexi", "captain", "kase",
    "kollections", "koncurrent", "kommander", "status"
).forEach { includeBuild("../$it") }

rootProject.name = "krest"

includeSubs("krest", ".", "core")
