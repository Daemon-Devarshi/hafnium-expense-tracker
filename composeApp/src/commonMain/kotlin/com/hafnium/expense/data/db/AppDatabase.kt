package com.hafnium.expense.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Main Room database for the Expense Tracker application.
 *
 * This database contains all the entities and DAOs needed for the app.
 */
@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}

