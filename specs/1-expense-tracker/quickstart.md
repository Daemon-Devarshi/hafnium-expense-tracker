# Quickstart: Expense Tracker (Kotlin Compose Multiplatform)

This plan uses:
- Shared UI: Compose Multiplatform (Android + iOS)
- Local storage: Room (KMP)
- DI: Koin
- Navigation: Voyager (separate navigation layer)
- Architecture: MVVM + Repository
- Future sync (optional): Ktorfit

Prerequisites
- Android Studio with KMP support; Xcode for iOS builds.
- JDK 17+

Install Dependencies (gradle/libs.versions.toml already defines Kotlin/Compose versions)
- Add the following libraries in composeApp/build.gradle.kts (or libs.versions.toml as needed):
  - Room KMP
  - Koin
  - Voyager

Suggested module structure (within composeApp)
- src/commonMain/
  - domain/models/Expense.kt
  - domain/usecase/
  - data/db/ (Room entities, DAO abstraction)
  - data/repository/ExpenseRepository.kt
  - di/Modules.kt (Koin)
  - navigation/ (Voyager routes/screens, NavGraph)
  - ui/screens/ (CaptureScreen, ListScreen)
  - ui/components/
  - viewmodel/ (CaptureViewModel, ListViewModel)
- src/androidMain/
  - data/db/ (Room driver/config)
  - data/image/ (JPEG compress/resize)
- src/iosMain/
  - data/db/ (Room driver/config)
  - data/image/ (JPEG compress/resize)

Data retention
- Keep expenses ≥ 1 year by default; make retention configurable. On app launch, run a cleanup use case to delete older data.

Build & Run
- Android: Use Gradle tasks or Android Studio run.
- iOS: Build with Xcode via Gradle-generated scheme or run the KMP iOS target.

Next Steps
- Implement Room schema (Expense entity, DAO), repository, ViewModels, and screens.
- Wire Koin modules and Voyager navigation.
- Add unit tests in commonTest for repository and ViewModels.

