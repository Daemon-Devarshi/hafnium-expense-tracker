package com.hafnium.expense.di

import com.hafnium.expense.data.db.DatabaseProvider
import com.hafnium.expense.data.image.ImageStorage
import com.hafnium.expense.data.image.ImageStorageIos
import org.koin.dsl.module

/**
 * iOS-specific Koin module.
 * Provides platform-specific implementations for database and image storage.
 */
fun iosModule() = module {
    // Database provider for iOS
    single {
        DatabaseProvider.getDatabase()
    }

    // Image storage provider for iOS
    single<ImageStorage> {
        ImageStorageIos()
    }
}
