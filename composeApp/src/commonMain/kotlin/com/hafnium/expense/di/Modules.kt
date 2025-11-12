package com.hafnium.expense.di

import com.hafnium.expense.data.repository.ExpenseRepositoryImpl
import com.hafnium.expense.domain.repository.ExpenseRepository
import org.koin.dsl.module

/**
 * Koin dependency injection modules for the Expense Tracker application.
 */

/**
 * Common module shared across all platforms.
 */
fun commonModule() = module {
    // Repository
    single<ExpenseRepository> {
        ExpenseRepositoryImpl(
            database = get(),
            imageStorage = get()
        )
    }
}

