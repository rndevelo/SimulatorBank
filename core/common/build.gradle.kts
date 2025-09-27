plugins {
    alias(libs.plugins.rndev.android.feature)
    alias(libs.plugins.rndev.di.library)
}

android {
    namespace = "io.rndev.core.common"
}

dependencies {
    implementation (libs.hilt.android)
}
