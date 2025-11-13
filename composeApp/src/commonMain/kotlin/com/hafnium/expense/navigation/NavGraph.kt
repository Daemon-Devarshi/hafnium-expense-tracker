package com.hafnium.expense.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

/**
 * Navigation graph setup for the Expense Tracker application.
 *
 * Configures Voyager navigation framework and defines navigation routes.
 */

object NavGraphSetup {

    /**
     * Configure Navigator with app-specific settings.
     */
    @Composable
    fun createNavigator(screens: List<Screen>) = Navigator(
        screens = screens
        // Start screen will be set in the UI layer
        // based on platform requirements
    )
}

/**
 * Navigation routes (will be expanded as screens are added).
 */
sealed class Route {
    data object CaptureList : Route()
    data class CaptureDetail(val expenseId: Long = 0) : Route()
}
