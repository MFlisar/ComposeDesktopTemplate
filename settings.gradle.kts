pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("multiplatform").version(extra["kotlin.version"] as String)
        kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.gradle.toolchains.foojay-resolver-convention").version(extra["foojay.version"] as String)
    }
}

include(":library")
include(":demo")
