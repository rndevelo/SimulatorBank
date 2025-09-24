plugins {
    alias(libs.plugins.rndev.android.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
}

android {
    namespace = "io.rndev.auth_data"
}

dependencies {
    implementation(project(":features:auth:auth-domain"))
    implementation (libs.hilt.android)
}
