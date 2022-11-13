import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "io.github.sgpublic"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
                implementation("org.slf4j:slf4j-api:2.0.3")

                val logback = "1.4.4"
                // https://mvnrepository.com/artifact/ch.qos.logback/logback-core
                implementation("ch.qos.logback:logback-core:$logback")
                // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
                implementation("ch.qos.logback:logback-classic:$logback")

                // https://mvnrepository.com/artifact/io.netty/netty-all
                implementation("io.netty:netty-all:4.1.85.Final")

                // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
                implementation("com.squareup.okhttp3:okhttp:4.10.0")

                // https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on
                implementation("org.bouncycastle:bcpkix-jdk15on:1.70")

                // https://mvnrepository.com/artifact/com.google.code.gson/gson
                implementation("com.google.code.gson:gson:2.10")

                // https://mvnrepository.com/artifact/org.ini4j/ini4j
                implementation("org.ini4j:ini4j:0.5.4")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.sgpublic.Application"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "BiliTurbo"
            packageVersion = version.toString()
        }
    }
}
