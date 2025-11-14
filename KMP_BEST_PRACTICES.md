# KMP Best Practices - Code Organization & Utilities

## Overview
This document outlines best practices for organizing reusable code in Kotlin Multiplatform (KMP) projects, with focus on extension functions and utility organization.

## SOLID Design Principles (applied to KMP)

SOLID is a collection of five object-oriented design principles that produce more maintainable, understandable, and flexible software. Below are concrete recommendations for applying each principle in a Kotlin Multiplatform project.

- Single Responsibility Principle (SRP)
  - Keep each module, class, file, or object focused on a single reason to change.
  - In KMP: separate domain models, repository contracts, persistence implementations, UI, and utilities into different packages/source sets.
  - Example: `Expense` (domain) vs `ExpenseEntity` (persistence) vs `ExpenseRepository` (contract).

- Open/Closed Principle (OCP)
  - Make modules open for extension but closed for modification.
  - In KMP: design public interfaces and rely on dependency injection (Koin) so you can add new implementations without changing existing code.
  - Example: add a new `ImageOptimizer` implementation and register it via DI without changing repository code.

- Liskov Substitution Principle (LSP)
  - Subtypes must be substitutable for their base types.
  - In KMP: ensure platform-specific `actual` implementations and repository implementations adhere strictly to the contracts defined in `commonMain` interfaces.
  - Example: `ImageStorageAndroid`/`ImageStorageIos` must behave consistently with `ImageStorage` expectations (save/delete semantics, error handling).

- Interface Segregation Principle (ISP)
  - Prefer small, role-specific interfaces over large monolithic ones.
  - In KMP: define narrow contracts in `commonMain` (e.g., `ImageStorage` has only `saveImage`, `deleteImage`, `exists`) and split if responsibilities grow.
  - Example: if image optimization and storage diverge, introduce `ImageOptimizer` and `ImageStorage` instead of merging both.

- Dependency Inversion Principle (DIP)
  - High-level modules should not depend on low-level modules; both should depend on abstractions.
  - In KMP: declare repositories and services as interfaces in `commonMain` and inject concrete implementations via DI from platform/source-set modules.
  - Example: `CaptureViewModel` depends on `ExpenseRepository` (interface) and receives a platform-registered implementation via Koin.

### Applying SOLID to this repository (practical checklist)
- [ ] SRP: Verify each package/file has one primary responsibility (domain, data, ui, di, util).
- [ ] OCP: Add interfaces and extension points before implementing features that may change.
- [ ] LSP: Create small integration tests that exercise `commonMain` contracts with platform implementations.
- [ ] ISP: Split large interfaces (if any) into smaller focused interfaces.
- [ ] DIP: Use DI (Koin) to wire implementations; never `new` concrete classes in `commonMain`.

### Dependency Injection Best Practices in KMP
- **Always specify types in Koin module declarations**: Use explicit types like `single<ImageStorage> { ImageStorageAndroid(androidContext()) }` instead of `single { ImageStorageAndroid(androidContext()) }` to ensure Koin can resolve dependencies correctly and prevent runtime injection failures.
- **Register platform-specific implementations in platform modules**: Keep commonMain focused on interfaces; provide concrete implementations in androidMain/iosMain modules.
- **Test DI setup**: Before committing, verify that all dependencies can be resolved by running the app or using Koin's dry-run features if available.

---

## Expect/Actual Classes Best Practices

To ensure smooth multiplatform development and avoid compilation errors:

- **Define `expect` in `commonMain`**: Place `expect` class, function, or property declarations only in the `commonMain` source set.
- **Provide `actual` in each platform**: Implement `actual` counterparts in every platform source set (e.g., `androidMain`, `iosMain`) where the expect is used.
- **Match signatures exactly**: Ensure the class name, constructor, and function signatures are identical between `expect` and `actual` declarations.
- **Avoid duplicates**: Do not declare multiple `actual` implementations of the same `expect` in the same source set.
- **Use for platform APIs**: Employ `expect`/`actual` when common code needs access to platform-specific APIs (e.g., image pickers, file storage).
- **Platform-only code**: If a class is only used in one platform and not accessed from common code, define it directly in the platform source set without `expect`/`actual`.
- **Validate after changes**: Always check for compilation errors after adding or modifying `expect`/`actual` pairs, and ensure the project builds successfully before committing.

---

## Extension Functions in KMP (Recommended Approach)

### ✅ Best Practice: Separate Utility File

**Location:**
```
composeApp/src/commonMain/kotlin/com/hafnium/expense/util/
├── DateExtensions.kt
├── StringExtensions.kt
├── CollectionExtensions.kt
└── ValidationExtensions.kt
```

**Benefits:**
- **Single Responsibility**: Each file has one purpose
- **Discoverability**: Easy to find related utilities
- **Reusability**: Import utilities wherever needed
- **Testability**: Easier to write unit tests
- **Idiomatic Kotlin**: Extension functions are the Kotlin way

**Example: DateExtensions.kt**
```kotlin
package com.hafnium.expense.util

import kotlinx.datetime.LocalDate

fun LocalDate.minusDays(days: Int): LocalDate { /* ... */ }
fun LocalDate.plusDays(days: Int): LocalDate { /* ... */ }
```

**Usage in multiple files:**
```kotlin
import com.hafnium.expense.util.minusDays
import com.hafnium.expense.util.plusDays

// Can be used in ListScreen, CaptureScreen, ViewModels, etc.
val previousDay = currentDate.minusDays(1)
val nextDay = currentDate.plusDays(1)
```

### ❌ Avoid: Static Utility Classes

Not recommended in Kotlin:
```kotlin
// ❌ NOT idiomatic Kotlin
object DateUtils {
    fun minusDays(date: LocalDate, days: Int): LocalDate { /* ... */ }
}

// Usage is verbose:
DateUtils.minusDays(currentDate, 1)
```

## Utility File Organization Pattern

### 1. **By Domain/Feature**
```
util/
├── expense/
│   ├── ExpenseCalculations.kt
│   └── ExpenseValidations.kt
├── date/
│   └── DateExtensions.kt
└── string/
    └── StringExtensions.kt
```

### 2. **By Type** (Current approach - recommended for small projects)
```
util/
├── DateExtensions.kt    (date-related)
├── StringExtensions.kt  (string-related)
├── CollectionExtensions.kt  (list/collection-related)
└── ValidationExtensions.kt  (validation-related)
```

### 3. **By Layer**
```
util/
├── domain/
│   └── DomainExtensions.kt
├── ui/
│   └── ComposableExtensions.kt
└── data/
    └── DataExtensions.kt
```

## Extension Function Naming Conventions

### ✅ Good Names
```kotlin
// Verb-based (clear action)
fun LocalDate.minusDays(days: Int): LocalDate
fun LocalDate.plusDays(days: Int): LocalDate
fun String.isValidEmail(): Boolean
fun List<Expense>.totalAmount(): Int
```

### ❌ Poor Names
```kotlin
// Unclear purpose
fun LocalDate.subtract(days: Int): LocalDate  // Could be hours/minutes
fun String.check(): Boolean  // What are we checking?
fun List<Expense>.calculate(): Int  // Calculate what?
```

## Technology Independence in KMP

### Extension Functions Enable Platform Independence
```kotlin
// util/DateExtensions.kt - CommonMain
// Works across Android, iOS, Desktop, Web
fun LocalDate.minusDays(days: Int): LocalDate {
    // Platform-agnostic implementation
    // No platform-specific imports
}
```

### Avoid Mixing Platforms
```kotlin
// ❌ DON'T - Breaks platform independence
fun LocalDate.format(): String {
    return SimpleDateFormat.getDateInstance().format(this)  // Android only!
}

// ✅ DO - Platform independent
fun LocalDate.format(): String {
    return this.toString()  // Uses kotlinx.datetime
    // Or use: this.format(LocalDate.Formats.ISO)
}
```

## Project Structure for This App

**Current Organization:**
```
composeApp/src/
├── commonMain/
│   ├── kotlin/com/hafnium/expense/
│   │   ├── domain/
│   │   │   ├── model/        (business entities)
│   │   │   └── repository/   (contracts)
│   │   ├── data/
│   │   │   ├── db/          (database layer)
│   │   │   ├── image/       (image storage)
│   │   │   └── repository/  (implementations)
│   │   ├── ui/
│   │   │   ├── viewmodel/   (state management)
│   │   │   ├── screens/     (UI composables)
│   │   │   └── components/  (reusable UI)
│   │   ├── di/              (dependency injection)
│   │   ├── navigation/      (routing)
│   │   └── util/            (utilities) ← HERE
│   │       ├── DateExtensions.kt
│   │       └── StringExtensions.kt
│   ├── androidMain/
│   └── iosMain/
```

## Best Practices Summary

| Practice | Reason |
|----------|--------|
| **Use extension functions** | Idiomatic Kotlin, cleaner syntax |
| **Create separate util files** | Better organization & discoverability |
| **One file per type** (for small projects) | Clear responsibility |
| **Package by feature** (for large projects) | Scale and maintainability |
| **Avoid platform-specific code** | Maintain KMP independence |
| **Add KDoc comments** | Self-documenting code |
| **Unit test extensions** | Ensure correctness across platforms |
| **Import with wildcards** | `import com.hafnium.expense.util.*` |

## Testing Utilities

```kotlin
// composeApp/src/commonTest/kotlin/com/hafnium/expense/util/DateExtensionsTest.kt
class DateExtensionsTest {
    @Test
    fun testMinusDays() {
        val date = LocalDate(2025, 11, 15)
        val result = date.minusDays(1)
        assertEquals(LocalDate(2025, 11, 14), result)
    }
    
    @Test
    fun testMinusDaysAcrossMonthBoundary() {
        val date = LocalDate(2025, 11, 1)
        val result = date.minusDays(1)
        assertEquals(LocalDate(2025, 10, 31), result)
    }
}
```

## References
- [Kotlin Official Docs - Extensions](https://kotlinlang.org/docs/extensions.html)
- [KMP Guidelines](https://kotlinlang.org/docs/multiplatform.html)
- [Jetpack Compose Best Practices](https://developer.android.com/jetpack/compose/best-practices)
