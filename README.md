SICEDroid — Kotlin Multiplatform
Aplicación para consulta académica del sistema SICENET del Tecnológico Nacional de México.
Desarrollada con Kotlin Multiplatform y Compose Multiplatform.
This is a Kotlin Multiplatform project targeting Android, Desktop (JVM).

/composeApp is for code that will be shared across your Compose Multiplatform applications.
It contains several subfolders:

commonMain is for code that is common for all targets, including UI, ViewModels, data models, parsers, Room database, and Ktor HTTP client.
androidMain is for Android-specific code such as the Room database builder with Android context and the MainActivity.
jvmMain is for Desktop (JVM)-specific code such as the Room database builder with a local file path and the desktop entry point.




Features

Login online and offline (uses local Room database as fallback)
Student profile view
Academic load (current semester subjects)
Kardex (academic history)
Final and unit grades with expandable unit badges


Tech Stack
LayerTechnologyUICompose MultiplatformNavigationorg.jetbrains.androidx.navigation 2.9.2HTTP ClientKtor 3.1.2Local DatabaseRoom KMP 2.7.0JSON Parsingkotlinx.serialization 1.7.3Annotation ProcessingKSP 2.3.6AsyncKotlin Coroutines

Project Structure
composeApp/src/
├── commonMain/kotlin/com/example/sicenetmultiplatform/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/          ← Room DAOs
│   │   │   ├── db/           ← SicenetDatabase + expect DatabaseBuilder
│   │   │   └── entity/       ← Room Entities
│   │   ├── mapper/           ← XML Parsers (Regex + kotlinx.serialization)
│   │   ├── model/            ← Domain models
│   │   ├── network/          ← Ktor client, SicenetService, SoapRequestBuilder
│   │   └── repository/       ← LocalRepository, NetworkRepository
│   ├── di/
│   │   └── AppContainer.kt   ← Dependency injection
│   ├── presentation/
│   │   ├── components/       ← Reusable UI components
│   │   ├── navigation/       ← AppNavigation, AppScaffold, Routes
│   │   ├── screens/          ← Login, Perfil, CargaAcademica, Cardex, Calificaciones
│   │   └── viewmodel/        ← ViewModels for each screen
│   ├── utils/
│   │   ├── XmlUtils.kt       ← Shared XML extraction utility
│   │   └── getCurrentTimeMillis.kt ← expect declaration
│   └── SessionManager.kt     ← Session handling
│
├── androidMain/kotlin/com/example/sicenetmultiplatform/
│   ├── data/local/db/
│   │   └── DatabaseBuilder.kt ← actual (Android Room with Context)
│   ├── utils/
│   │   └── TimeUtils.kt       ← actual (System.currentTimeMillis)
│   └── MainActivity.kt
│
└── jvmMain/java/com/example/sicenetmultiplatform/
    ├── data/local/db/
    │   └── DatabaseBuilder.kt ← actual (Room with user.home path)
    ├── utils/
    │   └── TimeUtils.kt       ← actual (System.currentTimeMillis)
    └── main.kt

Build and Run Android Application
To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE's toolbar or build it directly from the terminal:

on macOS/Linux

shell  ./gradlew :composeApp:assembleDebug

on Windows

shell  .\gradlew.bat :composeApp:assembleDebug
To install directly on a connected device:

on macOS/Linux

shell  ./gradlew :composeApp:installDebug

on Windows

shell  .\gradlew.bat :composeApp:installDebug

Build and Run Desktop (JVM) Application
To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:

on macOS/Linux

shell  ./gradlew :composeApp:run

on Windows

shell  .\gradlew.bat :composeApp:run
Package Desktop App
CommandOutputPlatform./gradlew :composeApp:packageMsi.msi installerWindows./gradlew :composeApp:packageExe.exe installerWindows./gradlew :composeApp:packageDmg.dmg installermacOS./gradlew :composeApp:packageDeb.deb packageLinux./gradlew :composeApp:createDistributableFolder with executableAll
Output is located at: composeApp/build/compose/binaries/main/

Offline Support
The app supports offline usage. On login, if no network connection is available, it falls back to locally stored credentials from the Room database. All academic data (profile, subjects, grades, kardex) is cached locally and displayed without internet connection after the first successful sync.

Authors

Erick Omar Pérez González — Android & KMP Development
Cristian — Calificaciones module
Aryemio — Diseño interfaz
