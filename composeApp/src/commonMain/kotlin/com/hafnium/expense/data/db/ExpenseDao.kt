package com.hafnium.expense.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for Expense entities.
 *
 * Provides database operations for expenses using Room.
 */
@Dao
interface ExpenseDao {
    
    /**
     * Insert a new expense.
     */
    @Insert
    suspend fun insert(expense: ExpenseEntity): Long
    
    /**
     * Update an existing expense.
     */
    @Update
    suspend fun update(expense: ExpenseEntity)
    
    /**
     * Delete an expense.
     */
    @Delete
    suspend fun delete(expense: ExpenseEntity)
    
    /**
     * Get an expense by ID.
     */
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getById(id: Long): ExpenseEntity?
    
    /**
     * Get all expenses for a specific date.
     * Returns a Flow for reactive updates.
     */
    @Query("SELECT * FROM expenses WHERE date = :date ORDER BY createdAt DESC")
    fun getByDate(date: String): Flow<List<ExpenseEntity>>
    
    /**
     * Get all expenses.
     */
    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC")
    fun getAll(): Flow<List<ExpenseEntity>>
    
    /**
     * Delete expenses older than a specific date.
     */
    @Query("DELETE FROM expenses WHERE date < :cutoffDate")
    suspend fun deleteOlderThan(cutoffDate: String): Int
}

