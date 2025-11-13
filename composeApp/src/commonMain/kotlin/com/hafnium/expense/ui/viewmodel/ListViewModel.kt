package com.hafnium.expense.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * ViewModel for the Expense list screen.
 *
 * Manages the list of expenses for a selected date, date switching,
 * and deletion of expenses.
 */
@OptIn(ExperimentalTime::class)
class ListViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    // ==================== State ====================

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // Load expenses for today by default
        val today = getTodayDate()
        loadExpensesForDate(today)
    }

    // ==================== Public Methods ====================

    /**
     * Change the date and reload expenses.
     */
    fun onDateChanged(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = newDate)
        loadExpensesForDate(newDate)
    }

    /**
     * Load expenses for a specific date.
     */
    fun loadExpensesForDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getExpensesByDate(date).collect { expenses ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        expenses = expenses,
                        isEmpty = expenses.isEmpty()
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    event = UiEvent.LoadError(e.message ?: "Failed to load expenses")
                )
            }
        }
    }

    /**
     * Delete an expense.
     */
    fun onExpenseDelete(expenseId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expenseId)
                // Reload the current date's expenses
                loadExpensesForDate(_uiState.value.selectedDate)
                _uiState.value = _uiState.value.copy(
                    event = UiEvent.DeleteSuccess(expenseId)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    event = UiEvent.DeleteError(e.message ?: "Failed to delete expense")
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

    private fun getTodayDate(): LocalDate {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    // ==================== Data Classes ====================

    /**
     * UI state for the list screen.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val selectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        val expenses: List<Expense> = emptyList(),
        val isEmpty: Boolean = true,
        val event: UiEvent? = null
    )

    /**
     * UI events for one-time actions (toasts, navigation, etc.).
     */
    sealed class UiEvent {
        data class DeleteSuccess(val expenseId: Long) : UiEvent()
        data class DeleteError(val message: String) : UiEvent()
        data class LoadError(val message: String) : UiEvent()
    }
}
