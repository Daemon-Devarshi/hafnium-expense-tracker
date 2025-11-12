package com.hafnium.expense.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

/**
 * Room entity representing an Expense in the database.
 *
 * This is the persistence layer representation that maps to the database table.
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String, // Stored as ISO-8601 string (YYYY-MM-DD)
    val amount: Int, // Amount in smallest currency unit
    val description: String = "",
    val imagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

