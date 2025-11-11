# Tasks: Expense Tracker MVP (KMP Compose)

Status: Draft | Owner: TBD | Start: 2025-11-11

Legend: [P1]=Must have for MVP, [P2]=Nice to have

1) Project wiring and dependencies [P1]
- Add dependencies to composeApp/build.gradle.kts:
  - Room KMP (runtime, compiler/ksp, coroutines)
  - Koin core
  - Voyager (navigator, bottomSheet if needed)
  - Kotlinx-datetime
- Acceptance: Gradle sync passes; app builds for Android+iOS.

2) Data model + Room schema [P1]
- Define Expense entity (id, date, amount, photoRef, createdAt, updatedAt)
- Define DAO (insert/update, query by date, get by id)
- Provide Database class and platform drivers (Android/iOS)
- Acceptance: Unit test inserts and retrieves an expense in commonTest (or Android unit if needed), schema migrates from v1.

3) Repository layer [P1]
- Implement ExpenseRepository with methods: add/update, getByDate(LocalDate), getById, retentionCleanup()
- Wrap image optimization interface (save/delete, compress+resize)
- Acceptance: Unit tests cover happy path + edge case (invalid amount rejected, photo failure handled).

4) DI with Koin [P1]
- Create Koin modules: databaseModule, repoModule, viewModelModule, imageModule
- Provide init function startKoin for shared
- Acceptance: App can start DI without errors; unit test resolves repository and VMs.

5) Navigation layer (Voyager) [P1]
- Define routes/screens: CaptureScreen, ListScreen
- Create NavGraph that wires initial route to CaptureScreen and allows navigating to ListScreen
- Acceptance: Smoke test navigates between screens without crash.

6) UI: Capture screen [P1]
- Fields: Date (default today, changeable), Amount (integer only), Photo (add/remove)
- Save button disabled until valid inputs
- Acceptance: Manual smoke on emulator/simulator; validation messages show; Save persists.

7) UI: List screen [P1]
- Default shows today’s expenses
- Date selector to change date
- Tap item to open Capture screen pre-filled
- Empty state message when none
- Acceptance: Manual smoke passes; list updates within ~1s for 200 items.

8) ViewModels (MVVM) [P1]
- CaptureViewModel: state, validation, save
- ListViewModel: load by date, select date, refresh
- Acceptance: Unit tests for happy path + one edge case per VM.

9) Data retention job [P1]
- On app start, run retentionCleanup (default 365 days, configurable)
- Acceptance: Unit test simulates old data and verifies deletion.

10) Image optimization [P1]
- Implement platform-specific image compress/resize to ≤ 1MB, long edge ≤ 1600px
- Acceptance: Manual test with large photo; repository handles failures gracefully.

11) Basic accessibility & i18n [P2]
- Content descriptions/labels; text scaling support for major UI components
- Acceptance: Spot-check with OS accessibility features.

12) Future sync scaffolding (deferred) [P2]
- Define Ktorfit API interface matching contracts/openapi.yaml
- Keep feature behind flag, with no-op implementation for MVP
- Acceptance: Compiles; repository can be extended later without breaking changes.

Deliverables
- Working Android+iOS app with shared UI
- Unit tests in composeApp/src/commonTest
- Updated README/quickstart if needed

