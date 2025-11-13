package com.hafnium.expense.data.repository

import com.hafnium.expense.data.db.AppDatabase
import com.hafnium.expense.data.db.ExpenseEntity
import com.hafnium.expense.data.image.ImageStorage
import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

/**
 * Implementation of ExpenseRepository.
 *
 * Handles persistence operations using Room database and image storage.
 */
class ExpenseRepositoryImpl(
    private val database: AppDatabase,
    private val imageStorage: ImageStorage
) : ExpenseRepository {
    
    private val expenseDao = database.expenseDao()
    
    override suspend fun saveExpense(expense: Expense): Long {
        val entity = expense.toEntity()
        return if (expense.id == 0L) {
            expenseDao.insert(entity)
        } else {
            expenseDao.update(entity)
            expense.id
        }
    }
    
    override suspend fun getExpenseById(id: Long): Expense? {
        return expenseDao.getById(id)?.toExpense()
    }
    
    override fun getExpensesByDate(date: LocalDate): Flow<List<Expense>> {
        return expenseDao.getByDate(date.toString()).map { entities ->
            entities.map { it.toExpense() }
        }
    }
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAll().map { entities ->
            entities.map { it.toExpense() }
        }
    }
    
    override suspend fun deleteExpense(expenseId: Long) {
        val entity = expenseDao.getById(expenseId) ?: return
        if (entity.imagePath != null) {
            imageStorage.deleteImage(entity.imagePath)
        }
        expenseDao.delete(entity)
    }
    
    override suspend fun deleteExpensesOlderThan(cutoffDate: LocalDate): Int {
        return expenseDao.deleteOlderThan(cutoffDate.toString())
    }
    
    // Conversion helpers
    private fun Expense.toEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            date = date.toString(),
            amount = amount,
            description = description,
            imagePath = imagePath,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
    
    private fun ExpenseEntity.toExpense(): Expense {
        return Expense(
            id = id,
            date = LocalDate.parse(date),
            amount = amount,
            description = description,
            imagePath = imagePath,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}

