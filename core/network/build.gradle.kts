plugins {
    alias(libs.plugins.rndev.android.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
}

android{
    namespace = "io.rndev.core.network"
}

dependencies {
    implementation(project(":core:common"))
}
