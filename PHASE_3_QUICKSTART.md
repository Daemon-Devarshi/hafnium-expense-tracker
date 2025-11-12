# Phase 3 Quick Start Guide: US1 - Add an Expense

## Objective
Enable users to create new expenses with:
- ✅ Date selector (default: today)
- ✅ Amount input (positive integer only)
- ✅ Optional photo attachment
- ✅ Save to local database

## Phase 3 Tasks (T021-T029)

### UI Layer - ViewModel
1. **T021**: Create `CaptureViewModel`
   - State: expense data, UI events, validation errors
   - Integration: ExpenseRepository (inject via Koin)
   - Use: StateFlow for reactive state

2. **T022**: Implement Validation
   - Date: required (default today)
   - Amount: positive integer
   - Show validation errors in UI

3. **T023**: Implement Save
   - Call `repository.saveExpense()`
   - Handle success/error
   - Navigate back on success

### UI Layer - Screen
4. **T024**: Create `CaptureScreen` Composable
   - Date picker input
   - Amount number input
   - Image preview (if selected)
   - Save/Cancel buttons
   - Display validation errors

5. **T025**: Image Picker Abstraction (Common)
   - Define interface/composable
   - Platform-specific implementations below

6. **T026**: Android Image Picker
   - Integrate with Android's image picker
   - Return selected image bytes

7. **T027**: iOS Image Picker
   - Integrate with iOS's image picker
   - Return selected image bytes

### Navigation & Testing
8. **T028**: Add Route & Navigation
   - Add CaptureScreen route in NavGraph
   - Wire navigation from list to capture
   - Set as launch destination

9. **T029**: Unit Tests
   - Test validation logic
   - Test save operation
   - Test error handling

---

## Key Dependencies Already Available

From Phase 2, you have:

```kotlin
// ✅ Already Available
val repository: ExpenseRepository    // Injected by Koin
val database: AppDatabase            // Behind repository
val imageStorage: ImageStorage       // For image persistence

// ✅ Models Ready
data class Expense(
    id: Long,
    date: LocalDate,
    amount: Int,
    imagePath: String?
)
```

---

## Architecture Pattern

```
CaptureScreen (UI)
    ↓
CaptureViewModel (State & Logic)
    ↓
ExpenseRepository (Data Access)
    ├→ AppDatabase (Persistence)
    └→ ImageStorage (Image Handling)
```

---

## Implementation Checklist

- [ ] **T021**: CaptureViewModel with state management
  - StateFlow<UiState>
  - onDateChanged(LocalDate)
  - onAmountChanged(Int)
  - onPhotoSelected(ByteArray)
  - onSave()
  - Events: Success, Error, Validation

- [ ] **T022**: Validation Logic
  - validateDate(): Boolean
  - validateAmount(): Boolean
  - showErrors(): Boolean

- [ ] **T023**: Save Logic
  - Create Expense object
  - Call repository.saveExpense()
  - Emit event (Success/Error)

- [ ] **T024**: CaptureScreen UI
  - DatePicker (Material 3)
  - OutlinedTextField for amount
  - Image preview area
  - Save/Cancel buttons
  - Error message display

- [ ] **T025**: Image Picker Interface
  - expect fun ImagePickerButton()
  - Callback: (ByteArray) -> Unit

- [ ] **T026**: Android Implementation
  - ActivityResultContracts.PickVisualMedia()
  - Read selected image
  - Resize/compress if needed

- [ ] **T027**: iOS Implementation
  - PHPickerViewController
  - Read selected image
  - Resize/compress if needed

- [ ] **T028**: Navigation
  - Add route in NavGraph
  - Pass expenseId parameter (null for new)
  - Navigation from list

- [ ] **T029**: Unit Tests
  - ValidationTest
  - SaveTest
  - ErrorHandlingTest

---

## Suggested Implementation Order

1. **Start with ViewModel** (T021-T023)
   - Define state, events, validation
   - Write save logic

2. **Build UI** (T024)
   - Form layout
   - State binding
   - Error display

3. **Image Picker** (T025-T027)
   - Common interface
   - Platform implementations

4. **Navigation** (T028)
   - Routes and parameter passing

5. **Tests** (T029)
   - Validation unit tests

---

## Files You'll Create in Phase 3

### Common Layer
```
composeApp/src/commonMain/kotlin/com/hafnium/expense/
├── ui/
│   ├── viewmodel/CaptureViewModel.kt        ← T021
│   ├── screens/CaptureScreen.kt             ← T024
│   └── components/ImagePicker.kt            ← T025
└── (navigation/NavGraph.kt already exists)  ← T028
```

### Android Layer
```
composeApp/src/androidMain/kotlin/com/hafnium/expense/
└── ui/
    └── components/ImagePickerAndroid.kt     ← T026
```

### iOS Layer
```
composeApp/src/iosMain/kotlin/com/hafnium/expense/
└── ui/
    └── components/ImagePickerIos.kt         ← T027
```

### Tests
```
composeApp/src/commonTest/kotlin/com/hafnium/expense/
└── ui/
    └── viewmodel/CaptureViewModelTest.kt    ← T029
```

---

## Repository Methods You'll Use

```kotlin
// From ExpenseRepository (Phase 2)
suspend fun saveExpense(expense: Expense): Long

// From ImageStorage (Phase 2)
suspend fun saveImage(imageData: ByteArray, filename: String): String?
```

---

## Testing Strategy

### Unit Tests (CaptureViewModelTest)
```kotlin
@Test
fun testValidateAmountPositive() { /* amount > 0 */ }

@Test
fun testValidateDateRequired() { /* date not null */ }

@Test
fun testSaveSuccessful() { /* repository.saveExpense called */ }

@Test
fun testSaveError() { /* error event emitted */ }
```

---

## Success Criteria

✅ User can:
1. Select a date (defaults to today)
2. Enter an amount (positive integer)
3. Optionally select a photo
4. Tap Save and see confirmation
5. Close the form and return to list

✅ Data:
1. Expense saved to database
2. Image saved to storage (if provided)
3. Both accessible in Phase 4 (List view)

---

**Ready to start Phase 3? Begin with T021: CaptureViewModel**

