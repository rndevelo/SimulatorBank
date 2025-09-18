// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint.gradle) apply false // NUEVO: Hacer el plugin Ktlint disponible
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

    // NUEVO: Placeholder para configuraciones comunes de Ktlint
    // Esto se aplicará a cualquier submódulo que aplique el plugin Ktlint.
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        // Ejemplo de configuración que podrías añadir aquí en el futuro:
        // extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        //     version.set("1.2.1") // Fija la versión de la herramienta Ktlint a usar
        //     // verbose.set(true)
        //     // android.set(true) // Habilita reglas específicas para Android si no se detectan automáticamente
        //     // reporters {
        //     //    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        //     //    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        //     //    reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.SARIF)
        //     // }
        // }
    }
}
