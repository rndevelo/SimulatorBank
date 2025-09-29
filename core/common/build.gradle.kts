plugins {
    alias(libs.plugins.rndev.android.feature)
    alias(libs.plugins.rndev.di.library)
    id("jacoco") // Aplicar el plugin de JaCoCo
}

// Configuración de la versión de JaCoCo
jacoco {
    toolVersion = "0.8.12"
}

android {
    namespace = "io.rndev.core.common"

    // Habilitar la cobertura de código para los build types relevantes
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
    implementation (libs.hilt.android)
}
