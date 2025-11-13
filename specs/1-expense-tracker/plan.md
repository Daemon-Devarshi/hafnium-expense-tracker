# Implementation Plan: Expense Tracker MVP

Branch: `[001-expense-tracker]` | Date: 2025-11-11 | Spec: ./spec.md
Input: Feature specification from `/specs/1-expense-tracker/spec.md`

Summary
- Build a simple expense tracker with shared UI on Android and iOS using Kotlin Compose Multiplatform.
- Store expenses locally using Room (KMP). DI with Koin. Navigation with Voyager via a separate navigation layer.
- MVVM + repository; design for a future optional sync path using Ktorfit.

Technical Context
- Language/Version: Kotlin 2.2.20 (per gradle/libs.versions.toml)
- Primary Dependencies: Compose Multiplatform 1.9.x; Koin; Voyager Navigation; Room (KMP); Image handling (platform-specific codecs wrapped in common API)
- Storage: Room Database (KMP). Single database: `expenses.db`, 1 table for MVP: `Expense` + optional `ImageRef` abstraction.
- Testing: kotlin-test; unit tests in commonTest for view-models and repository; instrumented tests deferred.
- Target Platform: Android (SDK 24+), iOS (Compose Multiplatform targets).
- Project Type: Mobile app (KMP shared UI).
- Performance Goals: 60 fps UI; launch to interactive ≤ 2s; date filter ≤ 1s for 200 items; image optimization ≤ 2s for 5 MB.
- Constraints: Offline-first; no network required; no authentication; on-device storage; configurable retention default 1 year.
- Scale/Scope: Single-user, local data only, up to thousands of expenses; 2 screens + edit flow.

Constitution Check
- Modularity: Shared modules: ui, domain, data; platform specifics isolated → PASS
- UI & State: Unidirectional data flow; MVVM; async with coroutines/Flows → PASS
- Testing Minimum Bar: Add unit tests for repo and VM happy path + one edge case → PASS
- Integration & Smoke: Build Android+iOS; smoke launch test → PASS
- Security/Privacy: No secrets; on-device only; minimal permissions → PASS
- Simplicity: Minimal deps; remove dead code; feature flags not required for MVP → PASS
- SOLID: Project follows SOLID design principles — SRP/ISP for small interfaces, DIP via DI, LSP contract testing, OCP for extensibility → PASS
- Compilation Requirement: Never commit code without successful compilation for Android and iOS targets → PASS

Project Structure

Documentation (this feature)
- specs/1-expense-tracker/
  - plan.md
  - research.md
  - data-model.md
  - quickstart.md
  - contracts/

Source Code (repository root)
- composeApp/
  - src/commonMain/
    - data/ (repositories, DB interfaces)
    - domain/ (models, use-cases)
    - ui/ (screens: CaptureScreen, ListScreen; components)
    - navigation/ (Voyager screens, routes, NavGraph)
    - di/ (Koin modules)
  - src/commonTest/ (unit tests: repository, viewmodels)
  - src/androidMain/ (Room Android driver, image IO)
  - src/iosMain/ (Room iOS driver, image IO)

Structure Decision
- Keep a single KMP module `composeApp` using the above internal folders. Separate navigation layer with Voyager screens and a NavGraph object.

Complexity Tracking
- Repository pattern: Chosen to isolate Room and future Ktorfit sync. Direct DB access rejected to keep UI/test simplicity and allow future sync without refactor.
