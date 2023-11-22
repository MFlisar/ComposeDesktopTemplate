import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                implementation(project(":library"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.michaelflisar.composedesktoptemplate.demo.MainKt"
        //javaHome = System.getenv("JDK_17")
        nativeDistributions {
            //targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            targetFormats(TargetFormat.Exe)
            packageName = "Compose Desktop Template Demo"
            packageVersion = "1.0.0"
        }
    }
}
