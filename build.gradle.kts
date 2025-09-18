// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
}

// Configure Detekt for all subprojects that apply the Detekt plugin
subprojects {
    plugins.withId("io.gitlab.arturbosch.detekt") {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            // Configure Detekt tasks
            jvmTarget = "11" // Setting JVM target for Detekt analysis

            // Configure reports directly on the task
            reports {
                html.required.set(true) // Ya suele ser true por defecto
                xml.required.set(true)  // Útil para CI
                txt.required.set(false)
                sarif.required.set(true) // Formato estándar para herramientas de análisis estático
            }
        }
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            // Archivo centralizado
            config.setFrom(rootProject.files("config/detekt/detekt.yml"))

            // Opcional: baseline para ignorar issues actuales
            // baseline = rootProject.file("config/detekt/detekt-baseline.xml")
            // The reports block has been moved to tasks.withType<Detekt>()
        }
    }
}
