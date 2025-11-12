package com.hafnium.expense.navigation

import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior

/**
 * Navigation graph setup for the Expense Tracker application.
 *
 * Configures Voyager navigation framework and defines navigation routes.
 */

object NavGraphSetup {

    /**
     * Configure Navigator with app-specific settings.
     */
    fun createNavigator() = Navigator(
        // Start screen will be set in the UI layer
        // based on platform requirements
        disposeBehavior = NavigatorDisposeBehavior.PopUntilRoot
    )
}

/**
 * Navigation routes (will be expanded as screens are added).
 */
sealed class Route {
    data object CaptureList : Route()
    data class CaptureDetail(val expenseId: Long = 0) : Route()
}

