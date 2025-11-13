package com.hafnium.expense.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Color palette for the Expense Tracker application.
 */
object ExpenseTrackerColors {
    val Primary = Color(0xFF2E7D32) // Green
    val Secondary = Color(0xFF1565C0) // Blue
    val Tertiary = Color(0xFFF57C00) // Orange
    
    val Error = Color(0xFFB3261E)
    val Surface = Color(0xFFFEFBFF)
    val Background = Color(0xFFFEFBFF)
}

/**
 * Light color scheme for the app.
 */
private val LightColorScheme = lightColorScheme(
    primary = ExpenseTrackerColors.Primary,
    secondary = ExpenseTrackerColors.Secondary,
    tertiary = ExpenseTrackerColors.Tertiary,
    error = ExpenseTrackerColors.Error,
    background = ExpenseTrackerColors.Background,
    surface = ExpenseTrackerColors.Surface,
)

/**
 * Dark color scheme for the app.
 */
private val DarkColorScheme = darkColorScheme(
    primary = ExpenseTrackerColors.Primary,
    secondary = ExpenseTrackerColors.Secondary,
    tertiary = ExpenseTrackerColors.Tertiary,
    error = ExpenseTrackerColors.Error,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
)

/**
 * Main theme for the Expense Tracker application.
 */
@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

