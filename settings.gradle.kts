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
        //id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
        //id("org.gradle.toolchains.foojay-resolver") version "0.7.0"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
        //id("org.gradle.toolchains.foojay-resolver-convention").version(extra["foojay.version"] as String)
    }

    /*toolchainManagement {
        jvm {
            javaRepositories {
                repository("foojay") {
                    resolverClass.set(org.gradle.toolchains.foojay.FoojayToolchainResolver::class.java)
                }
            }
        }
    }*/
}

include(":library")
include(":demo")
