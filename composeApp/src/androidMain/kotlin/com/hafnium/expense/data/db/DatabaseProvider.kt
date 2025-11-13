package com.hafnium.expense.data.db

import android.content.Context
import androidx.room.Room

/**
 * Android-specific database provider.
 *
 * Creates and manages the Room database instance for Android.
 */
object DatabaseProvider {
    private var instance: AppDatabase? = null
    
    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "hafnium_expense.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}

