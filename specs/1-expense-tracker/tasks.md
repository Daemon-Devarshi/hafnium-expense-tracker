# Tasks: Expense Tracker MVP (KMP Compose)

Status: Draft | Owner: TBD | Start: 2025-11-11

Legend: [P] = Parallelizable task, (US#) = User Story phase task

Phases
- Phase 1: Setup
- Phase 2: Foundational (blocking prerequisites)
- Phase 3: US1 – Add an expense (P1)
- Phase 4: US2 – View expenses by date (P1)
- Phase 5: US3 – Edit existing expense (P1)
- Phase 6: US4 – Photo optimization (P1)
- Phase 7: US5 – Data retention job (P1)
- Phase 8: Polish & Cross‑cutting (P2)
- Phase 9: Testing & Verification (P1)

Phase 1: Setup
- [X] T001 Update versions catalog for dependencies in gradle/libs.versions.toml
- [X] T002 Add KSP plugin alias and apply where needed in build.gradle.kts files (root and composeApp)
- [X] T003 Configure Compose Multiplatform, Kotlin, and Android/iOS targets in composeApp/build.gradle.kts
- [X] T004 Add dependencies to composeApp/build.gradle.kts (Room KMP, Koin, Voyager, kotlinx-datetime)

Phase 2: Foundational
- [X] T006 Create domain model Expense in composeApp/src/commonMain/kotlin/com/hafnium/expense/domain/model/Expense.kt
- [X] T007 Create Room entity and DAO in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/ExpenseEntity.kt
- [X] T008 Create ExpenseDao interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/ExpenseDao.kt
- [X] T009 Create AppDatabase in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/AppDatabase.kt
- [X] T010 Implement platform database providers (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/db/DatabaseProvider.android.kt
- [X] T011 Implement platform database providers (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/db/DatabaseProvider.ios.kt
- [X] T012 Define ImageStorage interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/image/ImageStorage.kt
- [X] T013 Implement ImageStorage (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/image/ImageStorageAndroid.kt
- [X] T014 Implement ImageStorage (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/image/ImageStorageIos.kt
- [X] T015 Create repository interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/domain/repository/ExpenseRepository.kt
- [X] T016 Implement repository in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/repository/ExpenseRepositoryImpl.kt
- [X] T017 Wire Koin modules in composeApp/src/commonMain/kotlin/com/hafnium/expense/di/Modules.kt
- [X] T018 Add Koin start function in composeApp/src/commonMain/kotlin/com/hafnium/expense/di/DI.kt
- [X] T019 Setup Voyager navigation shell in composeApp/src/commonMain/kotlin/com/hafnium/expense/navigation/NavGraph.kt
- [X] T020 [P] Create base UI theme/components in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/

Phase 3: US1 – Add an expense (P1)
Goal: User can add an expense with date (default today), integer amount, optional photo; Save persists locally.
Independent Test: Launch app, enter valid amount, optionally attach photo, Save; see item in today’s list.
- [ ] T021 (US1) Create CaptureViewModel in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/viewmodel/CaptureViewModel.kt
- [ ] T022 (US1) Implement validation (date required, amount positive integer) in CaptureViewModel
- [ ] T023 (US1) Implement save() in CaptureViewModel using ExpenseRepository
- [ ] T024 (US1) Create CaptureScreen UI in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/screens/CaptureScreen.kt
- [ ] T025 (US1) Add image picker integration stub (platform channels) in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/ImagePicker.kt
- [ ] T026 (US1) Android image picker wiring in composeApp/src/androidMain/kotlin/com/hafnium/expense/ui/components/ImagePickerAndroid.kt
- [ ] T027 (US1) iOS image picker wiring in composeApp/src/iosMain/kotlin/com/hafnium/expense/ui/components/ImagePickerIos.kt
- [ ] T028 (US1) Add route for CaptureScreen in NavGraph.kt
- [ ] T029 [P] (US1) Add minimal unit test for validation in composeApp/src/commonTest/kotlin/com/hafnium/expense/ui/viewmodel/CaptureViewModelTest.kt

Phase 4: US2 – View expenses by date (P1)
Goal: User can view list of expenses for a selected date (default today) and see empty state when none.
Independent Test: Change date and observe list updating; empty state visible if no items.
- [ ] T030 (US2) Create ListViewModel in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/viewmodel/ListViewModel.kt
- [ ] T031 (US2) Implement loadByDate(LocalDate) using ExpenseRepository
- [ ] T032 (US2) Create ListScreen UI in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/screens/ListScreen.kt
- [ ] T033 (US2) Add date selector component in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/DateSelector.kt
- [ ] T034 (US2) Add route for ListScreen in NavGraph.kt and set default date to today
- [ ] T035 [P] (US2) Unit test repository query-by-date in composeApp/src/commonTest/kotlin/com/hafnium/expense/data/repository/ExpenseRepositoryTest.kt

Phase 5: US3 – Edit existing expense (P1)
Goal: User taps an item in list to open capture form prefilled; editing amount/date and saving updates the entry.
Independent Test: From list, select item → edit → save → list reflects updated values without duplication.
- [ ] T036 (US3) Add navigation from ListScreen item to CaptureScreen with expense id in NavGraph.kt
- [ ] T037 (US3) CaptureViewModel loads existing expense when id provided
- [ ] T038 (US3) Repository upsert/update path (id present) in ExpenseRepositoryImpl.kt
- [ ] T039 (US3) UI: prefill CaptureScreen from ViewModel state

Phase 6: US4 – Photo optimization (P1)
Goal: Store attached photos optimized (≤ 1 MB, long edge ≤ 1600 px) and handle failures gracefully.
Independent Test: Attach a large photo, save; stored file size within targets; failure removes photo and saves expense.
- [ ] T040 (US4) Define ImageOptimizer interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/image/ImageOptimizer.kt
- [ ] T041 (US4) Implement ImageOptimizer (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/image/ImageOptimizerAndroid.kt
- [ ] T042 (US4) Implement ImageOptimizer (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/image/ImageOptimizerIos.kt
- [ ] T043 (US4) Integrate optimizer into repository save flow in ExpenseRepositoryImpl.kt
- [ ] T044 [P] (US4) Manual test procedure document in specs/1-expense-tracker/quickstart.md (photo optimization)

Phase 7: US5 – Data retention job (P1)
Goal: On app start, delete expenses older than configurable retention (default 365 days).
Independent Test: Seed older items; start app; verify old items deleted and recent items remain.
- [ ] T045 (US5) Add retention config and default in composeApp/src/commonMain/kotlin/com/hafnium/expense/config/RetentionConfig.kt
- [ ] T046 (US5) Implement retentionCleanup() in ExpenseRepositoryImpl.kt
- [ ] T047 (US5) Invoke cleanup on app start in composeApp/src/commonMain/kotlin/com/hafnium/expense/App.kt
- [ ] T048 [P] (US5) Unit test retention logic in composeApp/src/commonTest/kotlin/com/hafnium/expense/data/repository/RetentionTest.kt

Phase 8: Polish & Cross‑cutting (P2)
- [ ] T049 Add content descriptions/labels and focus order in UI components in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/
- [ ] T050 Ensure OS text scaling respected in major screens in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/
- [ ] T051 Android: manifest permissions (READ_MEDIA_IMAGES/READ_EXTERNAL_STORAGE, CAMERA if needed) in composeApp/src/androidMain/AndroidManifest.xml
- [ ] T052 iOS: Info.plist usage descriptions (NSPhotoLibraryAddUsageDescription/NSCameraUsageDescription) in iosApp/Info.plist
- [ ] T053 Smoke test script notes in specs/1-expense-tracker/quickstart.md (launch within 2s)
- [ ] T054 Define lightweight logging & privacy guidelines doc in specs/1-expense-tracker/logging.md (avoid sensitive data) [P]

Phase 9: Testing & Verification (P1)
- [ ] T055 Unit test ListViewModel date switching and empty state in composeApp/src/commonTest/kotlin/com/hafnium/expense/ui/viewmodel/ListViewModelTest.kt [P]
- [ ] T056 Unit test repository create/update & photo fallback in composeApp/src/commonTest/kotlin/com/hafnium/expense/data/repository/ExpenseRepositoryImplTest.kt [P]
- [ ] T057 Basic Android instrumentation smoke test (launch + add expense) in composeApp/src/androidInstrumentedTest/.../SmokeTest.kt
- [ ] T058 Performance timing harness (measure launch + date filter) script notes in specs/1-expense-tracker/perf.md [P]
- [ ] T059 Accessibility audit checklist doc in specs/1-expense-tracker/accessibility.md [P]

Dependencies (Story Order)
- Phase 1 → Phase 2 → US1 → US2 → US3
- US4 (Photo optimization) after T016/T023
- US5 (Retention) after T009/T016
- Phase 9 tests depend on related functionality (e.g., T055 after T030–T034)

Parallel Opportunities
- [P] T005, T020, T029, T035, T044, T048, T054, T055, T056, T058, T059
- Platform-specific tasks T026, T027, T041, T042 can proceed independently once interfaces exist

MVP Scope
- Minimum to ship: Phases 1–5 + critical tests (T029, T035, T055, T056). Phases 6–7 required per spec for full MVP. Phases 8–9 tighten quality.

Requirements Coverage
- F1–F5 (Capture form): T021–T028, T022/T023 (validation/save)
- F6–F10 (List & date filter): T030–T034, T055 (test)
- F11–F12 (Persistence model): T006–T016
- F13–F14 (Photo optimization & fallback): T040–T044, T056 (repo photo fallback test)
- F15 (No auth): implicit; no task (verified by absence) – document in logging guideline T054
- F16 (Retention config 1 year): T045–T048
- F17–F19 (Accessibility & UX): T049–T050, partial in T024/T032
- F20–F23 (Performance & reliability): T053 (smoke), T058 (perf harness), stability via tests T055–T056
- F24–F26 (Permissions, on‑device, log hygiene): T051–T054

Acceptance Scenario Mapping
- Scenario 1 (Add expense): T021–T024, T029 (test)
- Scenario 2 (Validation errors): T022, T029 (test)
- Scenario 3 (Change date & view list): T030–T034, T055 (test)
- Scenario 4 (Edit expense): T036–T039, T056 (test upsert)
- Scenario 5 (Photo optimization): T040–T044, T056 (fallback)
- Scenario 6 (Launch & smoke): T053 (script), T057 (instrumentation)

Risk & Mitigation
- Room KMP stability (alpha features) – Mitigation: isolate DB behind repository (T015–T016) allowing swap to SQLDelight if needed.
- Image optimization performance – Mitigation: configurable quality constants; measure via T058.
- iOS build integration for Koin/Voyager – Mitigation: early compile during Phase 2 (T005) and minimal wrappers.
- Retention cleanup timing – Mitigation: run once at startup (T047) with defensive try/catch.
- Navigation state mismatch when editing – Mitigation: central NavGraph (T019/T036) and ViewModel id loading (T037) tests.

Open Follow-ups (Post-MVP)
- Optional sync (Ktorfit) – future tasks not listed.
- Deletion flow – future spec/feature.

Format Validation
- All tasks follow pattern; new tasks T054–T059 appended with correct numbering.
