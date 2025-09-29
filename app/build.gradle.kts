plugins {
    alias(libs.plugins.rndev.android.application)
    alias(libs.plugins.rndev.android.application.compose)
    alias(libs.plugins.rndev.di.library.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint.gradle)
    alias(libs.plugins.kotlinxSerialization)
    id("jacoco") // Aplicar el plugin de JaCoCo directamente
}

// Configuración de la versión de JaCoCo
jacoco {
    toolVersion = "0.8.12"
}

android {
    namespace = "io.rndev.simulatorbank"

    defaultConfig {
        applicationId = "io.rndev.simulatorbank"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Habilitar la cobertura de código para el build type 'debug'
            enableUnitTestCoverage = true // Para tests unitarios (src/test)
            enableAndroidTestCoverage = true // Para tests instrumentados (src/androidTest)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            // Es buena práctica no habilitar la cobertura en release builds
            // enableUnitTestCoverage = false
            // enableAndroidTestCoverage = false
        }
    }
    // Especificar los tipos de test que JaCoCo debe considerar para la cobertura.
    // Esto es especialmente útil si tienes variantes de build complejas.
    // Para una configuración estándar, esto podría no ser estrictamente necesario
    // si `enableUnitTestCoverage` y `enableAndroidTestCoverage` funcionan como se espera.
    // No obstante, configurarlo explícitamente puede dar más control.
    /*
    testCoverage {
        jacocoVersion.set("0.8.12") // Redundante si ya está en jacoco { toolVersion = ... } pero no hace daño
    }
    */
}

dependencies {
    implementation(project(":features:auth:auth-presentation"))
    implementation(project(":features:auth:auth-domain"))
    implementation(project(":features:auth:auth-data"))
    implementation(project(":features:account:account-presentation"))
    implementation(project(":features:account:account-domain"))
    implementation(project(":features:account:account-data"))
    implementation(project(":features:detail:detail-presentation"))
    implementation(project(":features:detail:detail-domain"))
    implementation(project(":features:detail:detail-data"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //    Navigation3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

//    implementation("androidx.navigation:navigation-runtime-ktx:2.9.5")
//
//    implementation("androidx.navigation:navigation-compose:3.1.3")
}
