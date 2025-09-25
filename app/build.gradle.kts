plugins {
    alias(libs.plugins.rndev.android.application)
    alias(libs.plugins.rndev.android.application.compose)
    alias(libs.plugins.rndev.di.library.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.kotlinxSerialization)
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
    implementation(project(":features:auth:auth-presentation"))
    implementation(project(":features:auth:auth-domain"))
    implementation(project(":features:auth:auth-data"))
    implementation(project(":features:account:account-presentation"))
    implementation(project(":features:account:account-domain"))
    implementation(project(":features:account:account-data"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //    Navigation3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

//    implementation("androidx.navigation:navigation-runtime-ktx:2.9.5")
//
//    implementation("androidx.navigation:navigation-compose:3.1.3")
}
