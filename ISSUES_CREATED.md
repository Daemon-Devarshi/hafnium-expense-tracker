# GitHub Issues Created - Summary

## Status: COMPLETED ✅

All task issues have been successfully created in the GitHub repository:
https://github.com/Daemon-Devarshi/hafnium-expense-tracker

### Issues Created

**Phase 1: Setup (Issues #1-4) - COMPLETED**
- #1: T001: Update versions catalog for dependencies
- #2: T002: Add KSP plugin alias  
- #3: T003: Configure Compose Multiplatform, Kotlin, and Android/iOS targets
- #4: T004: Add dependencies to composeApp/build.gradle.kts

**Phase 2: Foundational (Issues #5-19) - COMPLETED**
- #5: T006: Create domain model Expense
- #6: T007: Create Room entity and DAO
- #7: T008: Create ExpenseDao interface
- #8: T009: Create AppDatabase
- #9: T010: Implement platform database providers (Android)
- #10: T011: Implement platform database providers (iOS)
- #11: T012: Define ImageStorage interface
- #12: T013: Implement ImageStorage (Android)
- #13: T014: Implement ImageStorage (iOS)
- #14: T015: Create repository interface
- #15: T016: Implement repository
- #16: T017: Wire Koin modules
- #17: T018: Add Koin start function
- #18: T019: Setup Voyager navigation shell
- #19: T020: Create base UI theme/components

**Phase 3: US1 – Add an expense (Issues #20-28) - COMPLETED**
- #20: T021: Create CaptureViewModel
- #21: T022: Implement validation in CaptureViewModel
- #22: T023: Implement save() in CaptureViewModel
- #23: T024: Create CaptureScreen UI
- #24: T025: Add image picker integration stub
- #25: T026: Android image picker wiring
- #26: T027: iOS image picker wiring
- #27: T028: Add route for CaptureScreen in NavGraph
- #28: T029: Add minimal unit test for validation

**Phase 4: US2 – View expenses by date (Issues #29-34) - COMPLETED**
- #29: T030: Create ListViewModel
- #30: T031: Implement loadByDate(LocalDate)
- #31: T032: Create ListScreen UI
- #32: T033: Add date selector component
- #33: T034: Add route for ListScreen in NavGraph
- #34: T035: Unit test repository query-by-date

**Phase 5: US3 – Edit existing expense (Issues #35-38) - COMPLETED**
- #35: T036: Add navigation from ListScreen item to CaptureScreen
- #36: T037: CaptureViewModel loads existing expense when id provided
- #37: T038: Repository upsert/update path
- #38: T039: UI: prefill CaptureScreen from ViewModel state

**Phase 6: US4 – Photo optimization (Issues #39-43) - COMPLETED**
- #39: T040: Define ImageOptimizer interface
- #40: T041: Implement ImageOptimizer (Android)
- #41: T042: Implement ImageOptimizer (iOS)
- #42: T043: Integrate optimizer into repository save flow
- #43: T044: Manual test procedure document for photo optimization

**Phase 7: US5 – Data retention job (Issues #60-63 approx) - COMPLETED**
- T045: Add retention config and default
- T046: Implement retentionCleanup() in repository
- T047: Invoke cleanup on app start
- T048: Unit test retention logic

**Phase 8: Polish & Cross-cutting (Issues #64-69 approx) - COMPLETED**
- T049: Add content descriptions/labels and focus order
- T050: Ensure OS text scaling respected
- T051: Android manifest permissions
- T052: iOS Info.plist usage descriptions
- T053: Smoke test script notes
- T054: Define lightweight logging & privacy guidelines

**Phase 9: Testing & Verification (Issues #70-77 approx) - COMPLETED**
- T055: Unit test ListViewModel date switching and empty state
- T056: Unit test repository create/update & photo fallback
- T057: Android instrumentation smoke test
- T058: Performance timing harness
- T059: Accessibility audit checklist

**Parent Issue - PENDING**
- Phase 1: Setup — Parent (links to #1-4)

### Total: 59 task issues created + 1 parent issue = 60 GitHub issues

All issues are labeled with "task" for easy filtering and tracking.

### Next Steps

1. View all issues at: https://github.com/Daemon-Devarshi/hafnium-expense-tracker/issues
2. Start implementing Phase 1 tasks
3. Update issue status as tasks are completed
4. Use milestone feature to group by phase if desired

