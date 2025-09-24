// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false // Asegurado que está activo
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false // Asegurado que está activo (probable dependencia para DI)
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint.gradle) apply false
}

// Configure Detekt for all subprojects that apply the Detekt plugin
subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            jvmTarget = "11"
            reports {
                html.required.set(true)
                xml.required.set(true)
                txt.required.set(false)
                sarif.required.set(true)
            }
        }
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        }
    }

    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        // Configuraciones comunes de Ktlint pueden ir aquí
    }
}
