package com.hafnium.expense.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hafnium.expense.util.daysInMonth
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

/**
 * Reusable date picker field component with calendar dialog.
 *
 * Features:
 * - Click to open calendar dialog
 * - Month navigation
 * - Today's date highlighted
 * - Selected date highlighted
 * - Error state support
 */
@Composable
fun DatePickerField(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Date",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val dateFormatter = LocalDate.Formats.ISO
    val dateString = selectedDate.format(dateFormatter)

    var showDialog by remember { mutableStateOf(false) }
    var currentMonth by remember { mutableStateOf(selectedDate) }

    if (showDialog) {
        DatePickerDialog(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onMonthChange = { currentMonth = it },
            onDateSelected = { date ->
                onDateSelected(date)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = dateString,
            onValueChange = { /* Read-only field */ },
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { showDialog = true },
            enabled = false,
            isError = isError,
            readOnly = true
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Calendar dialog for date selection.
 */
@Composable
private fun DatePickerDialog(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    onMonthChange: (LocalDate) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date") },
        text = {
            Column {
                // Month/Year header with navigation
                MonthNavigationHeader(
                    currentMonth = currentMonth,
                    onPreviousMonth = {
                        onMonthChange(currentMonth.minus(DatePeriod(months = 1)))
                    },
                    onNextMonth = {
                        onMonthChange(currentMonth.plus(DatePeriod(months = 1)))
                    }
                )

                // Day headers
                DayHeadersRow()

                // Calendar grid
                CalendarGrid(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Month navigation header with previous/next buttons.
 */
@Composable
private fun MonthNavigationHeader(
    currentMonth: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onPreviousMonth) {
            Text("<")
        }
        Text(
            "${currentMonth.month.name} ${currentMonth.year}",
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(onClick = onNextMonth) {
            Text(">")
        }
    }
}

/**
 * Row of day headers (Sun, Mon, Tue, etc.).
 */
@Composable
private fun DayHeadersRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Calendar grid showing days of the month.
 */
@OptIn(ExperimentalTime::class)
@Composable
private fun CalendarGrid(
    currentMonth: LocalDate,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val daysInMonth = daysInMonth(currentMonth.year, currentMonth.month.ordinal + 1)
    val firstDayOfMonth = LocalDate(currentMonth.year, currentMonth.month, 1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal // 0 = Monday in Kotlin
    val adjustedFirstDayOfWeek = (firstDayOfWeek + 1).mod(7) // Adjust to start with Sunday
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.height(240.dp)
    ) {
        // Empty cells for days before the first day of the month
        items(adjustedFirstDayOfWeek) {
            Box(modifier = Modifier.size(40.dp))
        }

        // Days of the month
        items(daysInMonth) { dayIndex ->
            val day = dayIndex + 1
            val date = LocalDate(currentMonth.year, currentMonth.month, day)
            val isSelected = date == selectedDate
            val isToday = date == today

            DayCell(
                day = day,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

/**
 * Individual day cell in the calendar.
 */
@Composable
private fun DayCell(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(onClick = onClick)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.surface
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

