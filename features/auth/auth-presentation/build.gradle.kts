plugins {
    alias(libs.plugins.rndev.android.feature)
    alias(libs.plugins.rndev.di.library.compose)
}

android {
    namespace = "io.rndev.auth_presentation"
}

dependencies {
    implementation(project(":features:auth:auth-domain"))
}
