# Tasks: Expense Tracker MVP (KMP Compose)

Status: Draft | Owner: TBD | Start: 2025-11-11

Legend: [P] = Parallelizable task, [US#] = User Story phase task

Phases
- Phase 1: Setup
- Phase 2: Foundational (blocking prerequisites)
- Phase 3: US1 – Add an expense (P1)
- Phase 4: US2 – View expenses by date (P1)
- Phase 5: US3 – Edit existing expense (P1)
- Phase 6: US4 – Photo optimization (P1)
- Phase 7: US5 – Data retention job (P1)
- Phase 8: Polish & Cross‑cutting (P2)

Phase 1: Setup
- [ ] T001 Update versions catalog for dependencies in gradle/libs.versions.toml
- [ ] T002 Add KSP plugin alias and apply where needed in build.gradle.kts files (root and composeApp)
- [ ] T003 Configure Compose Multiplatform, Kotlin, and Android/iOS targets in composeApp/build.gradle.kts
- [ ] T004 Add dependencies to composeApp/build.gradle.kts (Room KMP, Koin, Voyager, kotlinx-datetime)
- [ ] T005 [P] Verify Gradle sync and build for Android and iOS targets

Phase 2: Foundational
- [ ] T006 Create domain model Expense in composeApp/src/commonMain/kotlin/com/hafnium/expense/domain/model/Expense.kt
- [ ] T007 Create Room entity and DAO in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/ExpenseEntity.kt
- [ ] T008 Create ExpenseDao interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/ExpenseDao.kt
- [ ] T009 Create AppDatabase in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/db/AppDatabase.kt
- [ ] T010 Implement platform database providers (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/db/DatabaseProvider.android.kt
- [ ] T011 Implement platform database providers (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/db/DatabaseProvider.ios.kt
- [ ] T012 Define ImageStorage interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/image/ImageStorage.kt
- [ ] T013 Implement ImageStorage (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/image/ImageStorageAndroid.kt
- [ ] T014 Implement ImageStorage (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/image/ImageStorageIos.kt
- [ ] T015 Create repository interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/domain/repository/ExpenseRepository.kt
- [ ] T016 Implement repository in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/repository/ExpenseRepositoryImpl.kt
- [ ] T017 Wire Koin modules in composeApp/src/commonMain/kotlin/com/hafnium/expense/di/Modules.kt
- [ ] T018 Add Koin start function in composeApp/src/commonMain/kotlin/com/hafnium/expense/di/DI.kt
- [ ] T019 Setup Voyager navigation shell in composeApp/src/commonMain/kotlin/com/hafnium/expense/navigation/NavGraph.kt
- [ ] T020 [P] Create base UI theme/components in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/

Phase 3: US1 – Add an expense (P1)
Goal: User can add an expense with date (default today), integer amount, optional photo; Save persists locally.
Independent Test: Launch app, enter valid amount, optionally attach photo, Save; see item in today’s list.
- [ ] T021 [US1] Create CaptureViewModel in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/viewmodel/CaptureViewModel.kt
- [ ] T022 [US1] Implement validation (date required, amount positive integer) in CaptureViewModel
- [ ] T023 [US1] Implement save() in CaptureViewModel using ExpenseRepository
- [ ] T024 [US1] Create CaptureScreen UI in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/screens/CaptureScreen.kt
- [ ] T025 [US1] Add image picker integration stub (platform channels) in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/ImagePicker.kt
- [ ] T026 [US1] Android image picker wiring in composeApp/src/androidMain/kotlin/com/hafnium/expense/ui/components/ImagePickerAndroid.kt
- [ ] T027 [US1] iOS image picker wiring in composeApp/src/iosMain/kotlin/com/hafnium/expense/ui/components/ImagePickerIos.kt
- [ ] T028 [US1] Add route for CaptureScreen in NavGraph.kt
- [ ] T029 [P] [US1] Add minimal unit test for validation in composeApp/src/commonTest/kotlin/com/hafnium/expense/ui/viewmodel/CaptureViewModelTest.kt

Phase 4: US2 – View expenses by date (P1)
Goal: User can view list of expenses for a selected date (default today) and see empty state when none.
Independent Test: Change date and observe list updating; empty state visible if no items.
- [ ] T030 [US2] Create ListViewModel in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/viewmodel/ListViewModel.kt
- [ ] T031 [US2] Implement loadByDate(LocalDate) using ExpenseRepository
- [ ] T032 [US2] Create ListScreen UI in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/screens/ListScreen.kt
- [ ] T033 [US2] Add date selector component in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/components/DateSelector.kt
- [ ] T034 [US2] Add route for ListScreen in NavGraph.kt and set default date to today
- [ ] T035 [P] [US2] Unit test repository query-by-date in composeApp/src/commonTest/kotlin/com/hafnium/expense/data/repository/ExpenseRepositoryTest.kt

Phase 5: US3 – Edit existing expense (P1)
Goal: User taps an item in list to open capture form prefilled; editing amount/date and saving updates the entry.
Independent Test: From list, select item → edit → save → list reflects updated values without duplication.
- [ ] T036 [US3] Add navigation from ListScreen item to CaptureScreen with expense id in NavGraph.kt
- [ ] T037 [US3] CaptureViewModel loads existing expense when id provided
- [ ] T038 [US3] Repository upsert/update path (id present) in ExpenseRepositoryImpl.kt
- [ ] T039 [US3] UI: prefill CaptureScreen from ViewModel state

Phase 6: US4 – Photo optimization (P1)
Goal: Store attached photos optimized (≤ 1 MB, long edge ≤ 1600 px) and handle failures gracefully.
Independent Test: Attach a large photo, save; stored file size within targets; failure removes photo and saves expense.
- [ ] T040 [US4] Define ImageOptimizer interface in composeApp/src/commonMain/kotlin/com/hafnium/expense/data/image/ImageOptimizer.kt
- [ ] T041 [US4] Implement ImageOptimizer (Android) in composeApp/src/androidMain/kotlin/com/hafnium/expense/data/image/ImageOptimizerAndroid.kt
- [ ] T042 [US4] Implement ImageOptimizer (iOS) in composeApp/src/iosMain/kotlin/com/hafnium/expense/data/image/ImageOptimizerIos.kt
- [ ] T043 [US4] Integrate optimizer into repository save flow in ExpenseRepositoryImpl.kt
- [ ] T044 [P] [US4] Manual test procedure document in specs/1-expense-tracker/quickstart.md (photo optimization)

Phase 7: US5 – Data retention job (P1)
Goal: On app start, delete expenses older than configurable retention (default 365 days).
Independent Test: Seed older items; start app; verify old items deleted and recent items remain.
- [ ] T045 [US5] Add retention config and default in composeApp/src/commonMain/kotlin/com/hafnium/expense/config/RetentionConfig.kt
- [ ] T046 [US5] Implement retentionCleanup() in ExpenseRepositoryImpl.kt
- [ ] T047 [US5] Invoke cleanup on app start in composeApp/src/commonMain/kotlin/com/hafnium/expense/App.kt
- [ ] T048 [P] [US5] Unit test retention logic in composeApp/src/commonTest/kotlin/com/hafnium/expense/data/repository/RetentionTest.kt

Phase 8: Polish & Cross‑cutting (P2)
- [ ] T049 Add content descriptions/labels and focus order in UI components in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/
- [ ] T050 Ensure OS text scaling respected in major screens in composeApp/src/commonMain/kotlin/com/hafnium/expense/ui/
- [ ] T051 Android: manifest permissions (READ_MEDIA_IMAGES/READ_EXTERNAL_STORAGE, CAMERA if needed) in composeApp/src/androidMain/AndroidManifest.xml
- [ ] T052 iOS: Info.plist usage descriptions (NSPhotoLibraryAddUsageDescription/NSCameraUsageDescription) in iosApp/Info.plist
- [ ] T053 Smoke test script notes in specs/1-expense-tracker/quickstart.md (launch within 2s)

Dependencies (Story Order)
- Phase 1 → Phase 2 → US1 → US2 → US3
- US4 (Photo optimization) can begin after repository save path exists (T016/T023)
- US5 (Retention) can begin after repository and database exist (T009/T016)

Parallel Opportunities
- [P] T005 can run while writing non-overlapping files
- [P] T020 theming can proceed during foundation setup
- [P] T029, T035, T044, T048 are parallel test/docs tasks
- [P] Platform-specific image picker (T026, T027) can progress independently

MVP Scope
- Minimum to ship: Phases 1–5 (US1–US3). US4 and US5 included in MVP per spec but may be toggled by timeline. Polish is P2.

Format Validation
- All tasks follow: "- [ ] T### [P] [US#] Description with file path"
- Phase tasks are dependency-ordered and story-labeled where applicable
