package com.hafnium.expense.di

/**
 * Initializes Koin dependency injection.
 *
 * Call this during app startup to configure all dependencies.
 * Platform-specific implementations handle context setup.
 */
expect fun initKoin(context: Any? = null)
