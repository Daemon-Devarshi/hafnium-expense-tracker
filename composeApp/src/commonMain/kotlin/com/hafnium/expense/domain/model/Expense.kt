package com.hafnium.expense.domain.model

import kotlinx.datetime.LocalDate

/**
 * Domain model representing an Expense entry.
 *
 * This is the core business entity used throughout the application.
 * It is technology-independent and not tied to any persistence mechanism.
 */
data class Expense(
    val id: Long = 0,
    val date: LocalDate,
    val amount: Int, // Amount in smallest currency unit (e.g., cents)
    val description: String = "",
    val imagePath: String? = null, // Path to optimized image if present
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    init {
        require(amount > 0) { "Amount must be positive" }
    }
}

