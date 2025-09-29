plugins {
    alias(libs.plugins.rndev.jvm.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
    id("jacoco") // Aplicar el plugin de JaCoCo directamente
}

jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    implementation(project(":features:account:account-domain"))
    implementation(libs.kotlinx.coroutines.test)
}
