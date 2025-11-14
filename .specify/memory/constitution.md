# Hafnium Constitution

## Scope and Purpose
- This constitution defines the minimum bar for a mobile application targeting Android and iOS.
- It is technology independent and applies equally to cross‑platform (e.g., KMP, Flutter, React Native, Ionic) and fully native implementations.
- When a framework offers multiple ways to do something, prefer the simplest approach that satisfies these rules.

## Core Principles

### I. Modularity First
- Implement features as self‑contained modules with clear public interfaces; keep internals private.
- No cyclic dependencies; keep dependency graphs small and justified by need.
- Maximize shared logic where your chosen stack allows it; use platform‑specific code only when necessary (capabilities, UX, or performance).

### II. UI and State
- Use a clear separation of concerns between UI, state management, and side effects.
- Prefer unidirectional data flow (state goes down; events go up) via the idioms of your stack (MVVM/MVI/MVU/Redux‑style, etc.).
- Never block the UI thread. Use appropriate async primitives (threads/dispatch queues/coroutines/promises/streams) and respect component lifecycles.

### III. Testing Minimum Bar
- For every new feature or bug fix, add at least one unit test (and place tests according to repo conventions).
- Public module APIs require at least a happy‑path test; critical logic gets at least one edge‑case test.
- Keep tests fast and deterministic; isolate external I/O behind interfaces and use fakes/mocks where practical.

### IV. Integration and Device Smoke
- Each change must build for both Android and iOS targets using the project’s build system.
- Provide a smoke check proving the app launches to the first screen without crashing on a simulator/emulator for each platform.
- Call out backward‑incompatible changes in the change description and migration notes if needed.

### V. Observability, Security, Simplicity
- Log with structured, meaningful messages in debug builds; avoid logging secrets or sensitive data.
- Do not commit secrets. Use environment/secure storage and the platform’s recommended facilities for secrets and key material.
- Request only the permissions you actually need; explain use of user data clearly.
- Prefer simpler solutions over complex ones; remove dead code and unused dependencies promptly.
- Version public module APIs using semantic versioning where applicable.

## SOLID Design Principles (MANDATORY)
- The repository must follow SOLID principles as a minimum design standard across commonMain code and public interfaces.
- Single Responsibility Principle (SRP): Prefer small classes and single‑purpose interfaces. Move validation and business rules to domain services or validators rather than spreading them across UI/viewmodels and persistence layers.
- Open/Closed Principle (OCP): Prefer abstractions and extension points; add new behaviors via new implementations registered through DI rather than modifying existing code.
- Liskov Substitution Principle (LSP): Platform `actual` implementations and test doubles must be substitutable for their `expect`/interface contracts; behavior should be consistent.
- Interface Segregation Principle (ISP): Keep interfaces narrow; split interfaces when responsibilities diverge.
- Dependency Inversion Principle (DIP): Depend on abstractions defined in `commonMain`; inject concrete implementations from platform modules via DI.

Additional guidance:
- Make SOLID compliance part of code review criteria; reviewers must check for obvious SRP/ISP violations on changes.
- Add small contract tests to ensure LSP adherence for critical interfaces (e.g., `ImageStorage`, `ExpenseRepository`).

## Additional Constraints
- Platforms: Android and iOS.
- Performance: Do not block UI; keep startup lean; avoid unnecessary work during app launch; lazy‑load when reasonable.
- Accessibility: Provide accessible labels/traits, color contrast, and support for dynamic type/OS text scaling where feasible.
- Privacy: Collect only data that is necessary for features; document the purpose and retention; honor OS privacy controls.
- Internationalization: Use resource localization mechanisms supported by the chosen stack for user‑facing strings.

## Development Workflow and Quality Gates
- All changes land via code review; at least one reviewer approval is required.
- **Never commit code without successful compilation**: All changes must compile successfully for both Android and iOS targets before being committed to the repository.
- **Verify dependency injection setup**: Ensure all interfaces have concrete implementations registered in DI modules with correct types to prevent runtime injection failures.
- Quality gates for each change:
  - Builds succeed for Android and iOS targets.
  - Unit tests pass (including new/updated tests for the change).
  - Configured linters/formatters pass (use the repo's settings and scripts).
- The main branch remains releasable; use feature flags to hide incomplete work.
- Document notable behavior changes and update README/docs as needed.
