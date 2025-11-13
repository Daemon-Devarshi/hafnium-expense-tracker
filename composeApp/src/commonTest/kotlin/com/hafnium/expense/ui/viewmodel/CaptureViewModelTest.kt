package com.hafnium.expense.ui.viewmodel

import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for CaptureViewModel.
 *
 * Tests validation logic, state management, and user interactions.
 */
class CaptureViewModelTest {

    // Mock repository for testing
    private class MockRepository : ExpenseRepository {
        var savedExpense: Expense? = null
        var savedImageData: ByteArray? = null
        var savedImageFilename: String? = null

        override suspend fun saveExpense(expense: Expense): Long {
            savedExpense = expense
            return expense.id.takeIf { it > 0 } ?: 1L
        }

        override suspend fun getExpenseById(id: Long): Expense? {
            return if (id == 1L) {
                Expense(
                    id = 1L,
                    date = LocalDate(2025, 11, 13),
                    amount = 1500,
                    description = "Test expense",
                    imagePath = "/path/to/image.jpg"
                )
            } else {
                null
            }
        }

        override fun getExpensesByDate(date: LocalDate) = flowOf(emptyList<Expense>())

        override fun getAllExpenses() = flowOf(emptyList<Expense>())

        override suspend fun deleteExpense(expenseId: Long) {}

        override suspend fun deleteExpensesOlderThan(cutoffDate: LocalDate) = 0

        override suspend fun saveImage(imageData: ByteArray, filename: String): String? {
            savedImageData = imageData
            savedImageFilename = filename
            return "/path/to/saved/image.jpg"
        }
    }

    @Test
    fun testValidateAmountPositive() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Initially empty
        assertEquals("", viewModel.uiState.value.amountText)
        assertTrue(viewModel.uiState.value.validationErrors.isEmpty())

        // Set positive amount
        viewModel.onAmountChanged("1500")
        assertEquals("1500", viewModel.uiState.value.amountText)
        assertFalse(viewModel.uiState.value.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_AMOUNT))

        // Set zero (should error)
        viewModel.onAmountChanged("0")
        assertTrue(viewModel.uiState.value.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_AMOUNT))

        // Set negative (should error)
        viewModel.onAmountChanged("-100")
        assertTrue(viewModel.uiState.value.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_AMOUNT))
    }

    @Test
    fun testValidateDateRequired() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Default date should be today
        val defaultDate = viewModel.uiState.value.selectedDate
        assertEquals(defaultDate, viewModel.uiState.value.selectedDate)

        // Can change date
        val newDate = LocalDate(2025, 11, 10)
        viewModel.onDateChanged(newDate)
        assertEquals(newDate, viewModel.uiState.value.selectedDate)
    }

    @Test
    fun testPhotoSelection() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Initially no photo
        assertEquals(null, viewModel.uiState.value.selectedImageData)

        // Select photo
        val imageData = ByteArray(1024) { 1 }
        viewModel.onPhotoSelected(imageData)
        assertEquals(imageData, viewModel.uiState.value.selectedImageData)

        // Clear photo
        viewModel.onPhotoClear()
        assertEquals(null, viewModel.uiState.value.selectedImageData)
    }

    @Test
    fun testDescriptionUpdate() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Initially empty
        assertEquals("", viewModel.uiState.value.description)

        // Update description
        viewModel.onDescriptionChanged("Test expense for groceries")
        assertEquals("Test expense for groceries", viewModel.uiState.value.description)
    }

    @Test
    fun testSaveExpenseSuccessful() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Set up form
        viewModel.onAmountChanged("2500")
        viewModel.onDescriptionChanged("Lunch")

        // Save should succeed
        viewModel.onSave()

        // Wait a bit for the coroutine to complete
        kotlinx.coroutines.delay(100)

        // Check that expense was saved
        assertEquals(2500, repository.savedExpense?.amount)
        assertEquals("Lunch", repository.savedExpense?.description)
    }

    @Test
    fun testLoadExistingExpense() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository, expenseId = 1L)

        // Wait for load to complete
        kotlinx.coroutines.delay(100)

        // Check that existing expense is loaded
        assertEquals(1500, viewModel.uiState.value.amountText.toIntOrNull())
        assertEquals("Test expense", viewModel.uiState.value.description)
        assertEquals("/path/to/image.jpg", viewModel.uiState.value.existingImagePath)
    }

    @Test
    fun testEventHandling() = runTest {
        val repository = MockRepository()
        val viewModel = CaptureViewModel(repository)

        // Initially no event
        assertEquals(null, viewModel.uiState.value.event)

        // After consuming event, it should be null
        viewModel.onEventConsumed()
        assertEquals(null, viewModel.uiState.value.event)
    }
}

