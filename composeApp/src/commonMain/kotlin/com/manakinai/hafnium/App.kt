package com.manakinai.hafnium

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.hafnium.expense.ui.screens.ListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main application entry point.
 *
 * Launches with ListScreen (expense list view) as the default screen.
 * Uses Voyager Navigator for screen navigation.
 * Koin DI is initialized in MainActivity.
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        // Launch the app showing the ListScreen (expense list) by default
        // Users can view expenses for today and navigate to add new expenses
        Navigator(ListScreen())
    }
}