# KMP Best Practices - Code Organization & Utilities

## Overview
This document outlines best practices for organizing reusable code in Kotlin Multiplatform (KMP) projects, with focus on extension functions and utility organization.

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

