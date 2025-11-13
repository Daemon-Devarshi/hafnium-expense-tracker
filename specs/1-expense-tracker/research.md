# Research: Expense Tracker MVP

Date: 2025-11-11

Decision: Kotlin Compose Multiplatform shared UI
Rationale: Single codebase for UI across Android/iOS; reduces duplication; integrates with existing Gradle setup.
Alternatives considered: Separate native UIs (more effort), Flutter (would require new toolchain), React Native (JavaScript bridge performance overhead).

Decision: Room KMP for local persistence
Rationale: Strong typing, SQL schema migrations, multi-platform support emerging; fits simple relational needs.
Alternatives: SQLDelight (good KMP support, considered; Room chosen for familiarity), Realm (object DB; heavier), CoreData (iOS only).

Decision: Koin for DI
Rationale: Lightweight, KMP friendly; simple module declarations. Alternatives: Dagger/Hilt (Android-only heavy), manual service locator.

Decision: Voyager for navigation
Rationale: Multiplatform navigation library aligning with Compose; keeps navigation declarations unified.
Alternatives: Compose Navigation (Android-focused), manual stack management.

Decision: MVVM + Repository pattern
Rationale: Clear separation of concerns; testable ViewModels; repository enables future sync.
Alternatives: MVI (more boilerplate for simple flows), MVC (less testable), direct DB calls from ViewModel (tight coupling).

Decision: Image optimization via platform-specific resizing/compression
Rationale: Use native codecs for performance; wrap behind common interface.
Alternatives: Common image library (adds dependency weight), storing original (size bloat).

Decision: Data retention configurable (default 1 year)
Rationale: Meets requirement; simple periodic cleanup routine.
Alternatives: Unlimited retention (storage growth risk), shorter retention (may lose needed history).

Future Sync Path: Ktorfit for remote sync (deferred)
Rationale: Retrofit-like ergonomics in KMP; integrates with coroutines; easy to add later.
Alternatives: Ktor raw client (more code), GraphQL clients (overkill now).

Open Issues Resolved:
- Auth: None required.
- Time zones: Store date as local calendar date (no TZ shift); store createdAt/updatedAt epoch millis.
- IDs: Use UUID strings for simplicity.
- Image Format: Compress to JPEG (quality ~80) or PNG if transparency needed; prefer JPEG for photos.
- Cleanup Strategy: Background job on app start to remove expenses older than retention threshold.

SOLID Rationale and Mapping
- We adopt SOLID as a guiding design standard to improve maintainability and extensibility across the shared codebase.
- SRP: We keep domain logic (validations, business rules) in domain services and validators; VMs only handle UI state and orchestration.
- OCP: Interfaces (e.g., `ExpenseRepository`, `ImageStorage`) provide extension points so new behaviors can be added through DI without changing existing code.
- LSP: Platform `actual` implementations must behave consistently with `expect`/interface contracts; we plan small contract tests to validate behavior.
- ISP: Interfaces are intentionally narrow; when features expand we will split responsibilities into smaller interfaces.
- DIP: High-level modules (ViewModels/use-cases) depend only on abstractions defined in `commonMain`; platform modules provide concrete implementations.

This SOLID-driven approach reduces coupling and simplifies future additions (image optimizers, remote sync, analytics) while keeping the MVP implementation small and testable.
