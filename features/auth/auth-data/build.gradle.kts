plugins {
    alias(libs.plugins.rndev.android.library)
    alias(libs.plugins.rndev.jvm.retrofit)
    alias(libs.plugins.rndev.di.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
    id("jacoco") // Aplicar el plugin de JaCoCo directamente
}

jacoco {
    toolVersion = "0.8.12"
}

android {
    namespace = "io.rndev.data"

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        // release (u otros tipos) generalmente no tienen cobertura habilitada
        // release {
        //     enableUnitTestCoverage = false
        //     enableAndroidTestCoverage = false
        // }
    }
}

dependencies {
    implementation(project(":features:auth:auth-domain"))
    implementation(project(":core:common"))
}
