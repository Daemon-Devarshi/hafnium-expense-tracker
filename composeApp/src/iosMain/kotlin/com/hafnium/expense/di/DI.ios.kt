package com.hafnium.expense.di

import org.koin.core.context.startKoin

/**
 * iOS-specific Koin initialization.
 */
actual fun initKoin(context: Any?) {
    startKoin {
        modules(
            commonModule(),
            iosModule()
        )
    }
}

