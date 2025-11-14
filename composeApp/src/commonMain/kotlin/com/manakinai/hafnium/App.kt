package com.manakinai.hafnium

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.hafnium.expense.ui.screens.CaptureScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main application entry point.
 *
 * Launches with CaptureScreen (expense entry form) as the default screen.
 * Uses Voyager Navigator for screen navigation.
 * Koin DI is initialized in MainActivity.
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        // Launch the app showing the CaptureScreen (expense entry) by default
        // Users can add expenses immediately upon opening the app
        Navigator(CaptureScreen())
    }
}