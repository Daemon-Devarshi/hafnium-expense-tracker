package com.hafnium.expense.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Android-specific Koin initialization.
 */
actual fun initKoin(context: Any?) {
    startKoin {
        androidContext(context as Context)
        modules(
            commonModule(),
            androidModule()
        )
    }
}

