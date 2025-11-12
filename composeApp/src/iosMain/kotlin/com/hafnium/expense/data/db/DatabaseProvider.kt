package com.hafnium.expense.data.db

import androidx.room.Room
import platform.Foundation.NSHomeDirectory

/**
 * iOS-specific database provider.
 *
 * Creates and manages the Room database instance for iOS.
 */
object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                name = "${NSHomeDirectory()}/hafnium_expense.db",
                factory = { AppDatabase::class }
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}

