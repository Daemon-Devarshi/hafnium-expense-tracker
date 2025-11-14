package com.hafnium.expense.di

import com.hafnium.expense.data.db.DatabaseProvider
import com.hafnium.expense.data.image.ImageStorage
import com.hafnium.expense.data.image.ImageStorageAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific Koin module.
 * Provides platform-specific implementations for database and image storage.
 */
fun androidModule() = module {
    // Database provider for Android
    single {
        DatabaseProvider.getDatabase(androidContext())
    }

    // Image storage provider for Android
    single<ImageStorage> {
        ImageStorageAndroid(androidContext())
    }
}
