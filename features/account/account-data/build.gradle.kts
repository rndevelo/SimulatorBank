plugins {
    alias(libs.plugins.rndev.jvm.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
}

dependencies {
    implementation(project(":features:account:account-domain"))
}
