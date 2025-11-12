package com.hafnium.expense.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Initializes Koin dependency injection.
 *
 * Call this during app startup to configure all dependencies.
 */
fun initKoin(platformModule: Module) {
    startKoin {
        modules(
            commonModule(),
            platformModule
        )
    }
}

