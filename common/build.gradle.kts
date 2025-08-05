import org.gradle.api.JavaVersion

plugins {
    `maven-publish`
    id("com.android.library")
}

group = "dev.nullftc"
version = "1.0-SNAPSHOT"

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 24
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
    implementation(libs.ftc.inspection)
    implementation(libs.ftc.blocks)
    implementation(libs.ftc.robotcore)
    implementation(libs.ftc.server)
    implementation(libs.ftc.obj)
    implementation(libs.ftc.hw)
    implementation(libs.ftc.common)
    implementation(libs.ftc.vision)

    implementation(libs.joml)
    implementation(libs.gson)
}

afterEvaluate {
    publishing {
        repositories {
            mavenLocal()
        }

        publications {
            create("maven", MavenPublication::class.java) {
//                from(components["release"])
            }
        }
    }
}