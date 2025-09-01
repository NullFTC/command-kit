import org.gradle.api.JavaVersion

plugins {
    `maven-publish`
    id("com.android.library")
    kotlin("android")
}

group = "dev.nullftc.commandkit"
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    namespace = "dev.nullftc.commandkit"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    compileOnly(libs.ftc.robotcore)
    compileOnly(project(":common"))
    implementation(kotlin("stdlib"))
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
                from(components["release"])
            }
        }
    }
}
