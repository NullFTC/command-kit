import org.gradle.api.JavaVersion

plugins {
    `maven-publish`
    kotlin("android")
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

    kotlinOptions {
        jvmTarget = "11"
    }

    testOptions {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    namespace = "dev.nullftc.choreolib"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))

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

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

afterEvaluate {
    publishing {
        repositories {
            mavenLocal()
        }

        publications {
            create("maven", MavenPublication::class.java) {
                from(components["release"])
            }
        }
    }
}