plugins {
    id("io.github.rndevelo.buildlogic.android.application") version "1.0.2"
    id("io.github.rndevelo.buildlogic.android.application.compose") version "1.0.2"
    id("io.github.rndevelo.buildlogic.di.library.compose") version "1.0.2"
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
}

android {
    namespace = "io.rndev.simulatorbank"

    defaultConfig {
        applicationId = "io.rndev.simulatorbank"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
}
