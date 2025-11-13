package com.hafnium.expense.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Expense capture/edit screen.
 *
 * Manages form state, validation, and persistence of expense data.
 * Supports both creating new expenses and editing existing ones.
 */
@OptIn(ExperimentalTime::class)
class CaptureViewModel(
    private val repository: ExpenseRepository,
    private val expenseId: Long? = null
) : ViewModel() {

    // ==================== State ====================

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // If editing an existing expense, load it
        if (expenseId != null && expenseId > 0) {
            loadExpense(expenseId)
        }
    }

    // ==================== Public Methods ====================

    /**
     * Update the selected date.
     */
    fun onDateChanged(newDate: LocalDate) {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            selectedDate = newDate,
            validationErrors = currentState.validationErrors.toMutableMap().apply {
                remove(ValidationError.INVALID_DATE)
            }
        )
    }

    /**
     * Update the amount input.
     */
    fun onAmountChanged(newAmount: String) {
        val currentState = _uiState.value
        val amount = newAmount.toIntOrNull() ?: 0

        _uiState.value = currentState.copy(
            amountText = newAmount,
            validationErrors = currentState.validationErrors.toMutableMap().apply {
                if (amount <= 0 && newAmount.isNotEmpty()) {
                    put(ValidationError.INVALID_AMOUNT, "Amount must be positive")
                } else {
                    remove(ValidationError.INVALID_AMOUNT)
                }
            }
        )
    }

    /**
     * Update the description/notes field.
     */
    fun onDescriptionChanged(newDescription: String) {
        _uiState.value = _uiState.value.copy(description = newDescription)
    }

    /**
     * Handle photo selection from the image picker.
     */
    fun onPhotoSelected(imageData: ByteArray) {
        _uiState.value = _uiState.value.copy(
            selectedImageData = imageData,
            validationErrors = _uiState.value.validationErrors.toMutableMap().apply {
                remove(ValidationError.INVALID_IMAGE)
            }
        )
    }

    /**
     * Clear the selected photo.
     */
    fun onPhotoClear() {
        _uiState.value = _uiState.value.copy(selectedImageData = null)
    }

    /**
     * Validate the form and save the expense if valid.
     */
    fun onSave() {
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true)

                val amount = _uiState.value.amountText.toIntOrNull() ?: 0
                val imagePath = saveImageIfNeeded()

                val expense = Expense(
                    id = expenseId?.takeIf { it > 0 } ?: 0,
                    date = _uiState.value.selectedDate,
                    amount = amount,
                    description = _uiState.value.description,
                    imagePath = imagePath
                )

                val savedId = repository.saveExpense(expense)

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    event = UiEvent.SaveSuccess(savedId)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    event = UiEvent.SaveError(e.message ?: "Unknown error")
                )
            }
        }
    }

    /**
     * Dismiss the last event (e.g., after showing a toast).
     */
    fun onEventConsumed() {
        _uiState.value = _uiState.value.copy(event = null)
    }

    // ==================== Private Methods ====================

    /**
     * Load an existing expense for editing.
     */
    private fun loadExpense(id: Long) {
        viewModelScope.launch {
            try {
                val expense = repository.getExpenseById(id) ?: return@launch

                _uiState.value = _uiState.value.copy(
                    selectedDate = expense.date,
                    amountText = expense.amount.toString(),
                    description = expense.description,
                    existingImagePath = expense.imagePath,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    event = UiEvent.LoadError(e.message ?: "Failed to load expense")
                )
            }
        }
    }

    /**
     * Save the selected image if one was provided.
     */
    private suspend fun saveImageIfNeeded(): String? {
        val imageData = _uiState.value.selectedImageData ?: return _uiState.value.existingImagePath

        return try {
            val timestamp = Clock.System.now().toEpochMilliseconds()
            val filename = "expense_${_uiState.value.selectedDate}_${timestamp}.jpg"
            repository.saveImage(imageData, filename)
        } catch (ex: Exception) {
            null // Image save is optional, don't fail the whole operation
        }
    }

    /**
     * Validate all form fields.
     */
    private fun validateForm(): Boolean {
        val errors = mutableMapOf<ValidationError, String>()

        // Validate date (required, must not be in future)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (_uiState.value.selectedDate > now) {
            errors[ValidationError.INVALID_DATE] = "Date cannot be in the future"
        }

        // Validate amount (required, must be positive)
        val amount = _uiState.value.amountText.toIntOrNull() ?: 0
        if (amount <= 0) {
            errors[ValidationError.INVALID_AMOUNT] = "Amount must be a positive number"
        }

        _uiState.value = _uiState.value.copy(validationErrors = errors)
        return errors.isEmpty()
    }

    // ==================== Data Classes ====================

    /**
     * UI state for the capture screen.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val selectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        val amountText: String = "",
        val description: String = "",
        val selectedImageData: ByteArray? = null,
        val existingImagePath: String? = null,
        val validationErrors: Map<ValidationError, String> = emptyMap(),
        val event: UiEvent? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is UiState) return false
            return isLoading == other.isLoading &&
                    isSaving == other.isSaving &&
                    selectedDate == other.selectedDate &&
                    amountText == other.amountText &&
                    description == other.description &&
                    selectedImageData.contentEquals(other.selectedImageData) &&
                    existingImagePath == other.existingImagePath &&
                    validationErrors == other.validationErrors &&
                    event == other.event
        }

        override fun hashCode(): Int {
            var result = isLoading.hashCode()
            result = 31 * result + isSaving.hashCode()
            result = 31 * result + selectedDate.hashCode()
            result = 31 * result + amountText.hashCode()
            result = 31 * result + description.hashCode()
            result = 31 * result + (selectedImageData?.contentHashCode() ?: 0)
            result = 31 * result + (existingImagePath?.hashCode() ?: 0)
            result = 31 * result + validationErrors.hashCode()
            result = 31 * result + (event?.hashCode() ?: 0)
            return result
        }
    }

    /**
     * Validation error types.
     */
    enum class ValidationError {
        INVALID_DATE,
        INVALID_AMOUNT,
        INVALID_IMAGE
    }

    /**
     * UI events for one-time actions (toasts, navigation, etc.).
     */
    sealed class UiEvent {
        data class SaveSuccess(val expenseId: Long) : UiEvent()
        data class SaveError(val message: String) : UiEvent()
        data class LoadError(val message: String) : UiEvent()
    }
}
