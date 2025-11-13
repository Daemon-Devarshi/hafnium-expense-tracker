package com.hafnium.expense.domain.model

import kotlinx.datetime.Clock
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
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds()
) {
    init {
        require(amount > 0) { "Amount must be positive" }
    }
}
