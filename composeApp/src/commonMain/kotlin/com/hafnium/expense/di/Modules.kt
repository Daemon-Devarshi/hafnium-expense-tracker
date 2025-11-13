package com.hafnium.expense.di

import com.hafnium.expense.data.repository.ExpenseRepositoryImpl
import com.hafnium.expense.domain.repository.ExpenseRepository
import com.hafnium.expense.ui.viewmodel.CaptureViewModel
import com.hafnium.expense.ui.viewmodel.ListViewModel
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

    // ViewModels (factory for multiple instances with different parameters)
    factory { (expenseId: Long?) ->
        CaptureViewModel(
            repository = get(),
            expenseId = expenseId
        )
    }

    factory {
        ListViewModel(repository = get())
    }
}

