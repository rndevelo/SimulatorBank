plugins {
    alias(libs.plugins.rndev.android.feature)
    alias(libs.plugins.rndev.di.library.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
}

android {
    namespace = "io.rndev.auth_presentation"
}

dependencies {
    implementation(project(":features:auth:auth-domain"))
}
