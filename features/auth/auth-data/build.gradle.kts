plugins {
    alias(libs.plugins.rndev.android.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
}

android {
    namespace = "io.rndev.data"
}

dependencies {
    implementation(project(":features:auth:auth-domain"))
    implementation(project(":core:common"))
}
