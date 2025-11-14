package com.hafnium.expense.util

import kotlinx.datetime.LocalDate

/**
 * Date utility extensions for common date arithmetic operations.
 *
 * These extensions are technology-independent and can be used across
 * all screens and viewmodels for consistent date handling.
 */

/**
 * Subtract days from the current date, accounting for month boundaries.
 *
 * @param days Number of days to subtract
 * @return New LocalDate after subtracting days
 */
fun LocalDate.minusDays(days: Int): LocalDate {
    return try {
        val date = this
        val newDay = date.day - days
        if (newDay > 0) {
            LocalDate(date.year, date.month, newDay)
        } else {
            // Go to previous month
            val previousMonthOrdinal = if (date.month.ordinal == 0) 11 else date.month.ordinal - 1
            val previousYear = if (date.month.ordinal == 0) date.year - 1 else date.year
            val daysInPreviousMonth = daysInMonth(previousYear, previousMonthOrdinal + 1)
            LocalDate(previousYear, previousMonthOrdinal + 1, daysInPreviousMonth + newDay)
        }
    } catch (_: Exception) {
        this
    }
}

/**
 * Add days to the current date, accounting for month boundaries.
 *
 * @param days Number of days to add
 * @return New LocalDate after adding days
 */
fun LocalDate.plusDays(days: Int): LocalDate {
    return try {
        val date = this
        val newDay = date.day + days
        val daysInCurrentMonth = daysInMonth(date.year, date.month.ordinal + 1)
        if (newDay <= daysInCurrentMonth) {
            LocalDate(date.year, date.month, newDay)
        } else {
            // Go to next month
            val nextMonthOrdinal = if (date.month.ordinal == 11) 0 else date.month.ordinal + 1
            val nextYear = if (date.month.ordinal == 11) date.year + 1 else date.year
            LocalDate(nextYear, nextMonthOrdinal + 1, newDay - daysInCurrentMonth)
        }
    } catch (_: Exception) {
        this
    }
}

/**
 * Get the number of days in a specific month/year.
 *
 * @param year The year
 * @param month The month (1-12)
 * @return Number of days in that month
 */
internal fun daysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 28
    }
}

/**
 * Check if a year is a leap year.
 *
 * @param year The year to check
 * @return True if leap year, false otherwise
 */
internal fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

