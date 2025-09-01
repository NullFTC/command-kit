import org.gradle.api.JavaVersion

plugins {
    `maven-publish`
    id("com.android.library")
}

group = "dev.nullftc.commandkit"
version = "1.0-SNAPSHOT"

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    namespace = "dev.nullftc.commandkit"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(libs.ftc.inspection)
    compileOnly(libs.ftc.blocks)
    compileOnly(libs.ftc.robotcore)
    compileOnly(libs.ftc.server)
    compileOnly(libs.ftc.obj)
    compileOnly(libs.ftc.hw)
    compileOnly(libs.ftc.common)
    compileOnly(libs.ftc.vision)

    compileOnly(libs.joml)
    compileOnly(libs.gson)
}

afterEvaluate {
    publishing {
        repositories {
            maven {
                name = "nullftcReleases"
                url = uri("https://maven.nullftc.dev/releases")
                credentials {
                    username = System.getenv("MAVEN_NAME") ?: ""
                    password = System.getenv("MAVEN_SECRET") ?: ""
                }
            }
        }

        publications {
            create("maven", MavenPublication::class.java) {
                from(components.findByName("release"))
            }
        }
    }
}