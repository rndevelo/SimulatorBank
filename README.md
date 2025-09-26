# 🏦 Simulator Bank  

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blueviolet?logo=kotlin&logoColor=white)  
![Compose](https://img.shields.io/badge/Jetpack%20Compose-%20UI-blue?logo=jetpackcompose&logoColor=white)  
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%7C%20MVVM-green)  
![Detekt](https://img.shields.io/badge/Code%20Quality-Detekt-orange)  
![Lint](https://img.shields.io/badge/Lint-Ktlint-yellow)  
![Testing](https://img.shields.io/badge/Testing-JUnit%20%7C%20MockK%20%7C%20Turbine-lightgrey)  
![License](https://img.shields.io/badge/License-MIT-brightgreen)  

Aplicación bancaria simulada, organizada bajo un enfoque **modular, escalable y mantenible**.  
Implementa **Clean Architecture**, **MVVM**, **Jetpack Compose**, **Retrofit**, **Mockoon**, **Detekt**, **Testing** y buenas prácticas de accesibilidad.

---

## 📑 Tabla de contenidos

1. [Introducción](#-introducción)  
2. [Principios arquitectónicos](#-principios-arquitectónicos)  
3. [Estructura modular por features](#-estructura-modular-por-features)  
4. [Tecnologías y librerías utilizadas](#-tecnologías-y-librerías-utilizadas)  
5. [Flujo de navegación / UI](#-flujo-de-navegación--ui)  
6. [Accesibilidad](#-accesibilidad)  
7. [Interceptores y manejo de red](#-interceptores-y-manejo-de-red)  
8. [Mockeo / entorno local (Mockoon)](#-mockeo--entorno-local-mockoon)  
9. [Testing](#-testing)  
10. [Calidad del código (Detekt / Lint)](#-calidad-del-código-detekt--lint)  
11. [Guía rápida de uso](#-guía-rápida-de-uso)  
12. [Contribuciones / estilo](#-contribuciones--estilo)  
13. [Contacto / licencia](#-contacto--licencia)  

---

## 🔹 Introducción

**Simulator Bank** es una aplicación simulada de banca móvil con funcionalidades típicas como:

- Login y autenticación  
- Consulta de cuentas y balances  
- Historial de transacciones  
- Transferencias  
- Perfil de usuario  

El proyecto está pensado como **entorno de aprendizaje y demostración**, aplicando arquitectura limpia, modularización por features y las últimas tecnologías de Android.

---

## 🔹 Principios arquitectónicos

- **Clean Architecture**: separación clara entre capas (data, domain, presentation).  
- **Modularización por features**: cada funcionalidad en su propio módulo independiente.  
- **MVVM**: patrón de presentación para desacoplar lógica de UI.  
- **Inyección de dependencias**: Hilt.  
- **UI declarativa con Jetpack Compose**.  
- **Networking con Retrofit + interceptores**.  
- **Mockeo de backend con Mockoon**.  
- **Testing a todos los niveles**.  
- **Accesibilidad incorporada**.  
- **Calidad de código con Detekt / ktlint**.  

---

## 🔹 Estructura modular por features

```bash
/app
/config
/features
    ├── feature-auth
    │     ├── data         # repositorios, fuentes remotas/locales, mappers
    │     ├── domain       # casos de uso, entidades
    │     └── presentation # UI (Compose), ViewModels
    ├── feature-accounts
    └── feature-detail
/common
/core
    ├── network            # Retrofit, interceptores
    ├── di                 # módulos de inyección globales
    ├── util               # extensiones, helpers
    └── testing            # utilidades para tests

