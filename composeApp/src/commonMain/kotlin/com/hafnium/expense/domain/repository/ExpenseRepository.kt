package com.hafnium.expense.domain.repository

import com.hafnium.expense.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Repository interface for Expense operations.
 *
 * Provides the contract for accessing and managing expenses.
 * Implementation handles the details of data persistence and retrieval.
 */
interface ExpenseRepository {

    /**
     * Create or update an expense.
     *
     * @param expense The expense to save
     * @return The ID of the saved expense
     */
    suspend fun saveExpense(expense: Expense): Long

    /**
     * Get an expense by ID.
     *
     * @param id The expense ID
     * @return The expense, or null if not found
     */
    suspend fun getExpenseById(id: Long): Expense?

    /**
     * Get all expenses for a specific date.
     *
     * @param date The date to query
     * @return Flow emitting list of expenses for the date
     */
    fun getExpensesByDate(date: LocalDate): Flow<List<Expense>>

    /**
     * Get all expenses.
     *
     * @return Flow emitting all expenses sorted by date descending
     */
    fun getAllExpenses(): Flow<List<Expense>>

    /**
     * Delete an expense.
     *
     * @param expenseId The ID of the expense to delete
     */
    suspend fun deleteExpense(expenseId: Long)

    /**
     * Delete all expenses older than a specified date.
     * Used for data retention cleanup.
     *
     * @param cutoffDate Expenses older than this date will be deleted
     * @return Number of expenses deleted
     */
    suspend fun deleteExpensesOlderThan(cutoffDate: LocalDate): Int
}

