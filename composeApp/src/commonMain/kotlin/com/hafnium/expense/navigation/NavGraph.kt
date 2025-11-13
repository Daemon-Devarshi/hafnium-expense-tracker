package com.hafnium.expense.navigation

/**
 * Navigation graph setup for the Expense Tracker application.
 *
 * Configures Voyager navigation framework and defines navigation routes.
 */

/**
 * Navigation routes for the application.
 */
sealed class Route {
    data object CaptureList : Route()
    data class CaptureDetail(val expenseId: Long = 0) : Route()
}
