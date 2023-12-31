plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("maven-publish")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
        //vendor = JvmVendorSpec.ADOPTOPENJDK
        //implementation = JvmImplementation.VENDOR_SPECIFIC
    }
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {

                implementation(compose.desktop.currentOs)

                //implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
                //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

                // Aurora - Windows Only Theming and Components
                //api("org.pushing-pixels:aurora-theming:1.3.0")
                //api("org.pushing-pixels:aurora-component:1.3.0")
                //api("org.pushing-pixels:aurora-window:1.3.0")
                api("org.pushing-pixels:aurora-theming:2.0-SNAPSHOT")
                api("org.pushing-pixels:aurora-component:2.0-SNAPSHOT")
                api("org.pushing-pixels:aurora-window:2.0-SNAPSHOT")

                // Icons
                api("org.jetbrains.compose.material:material-icons-extended-desktop:" + extra["compose.version"])

                // Excel/CSV
                //api("org.apache.poi:poi:5.0.0")
                //api("org.apache.poi:poi-ooxml:5.0.0")
                //api("com.opencsv:opencsv:5.6")

            }
        }
    }
}
