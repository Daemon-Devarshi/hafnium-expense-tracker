package com.hafnium.expense.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hafnium.expense.util.minusDays
import com.hafnium.expense.util.plusDays
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

/**
 * DateSelector component for navigating between dates.
 *
 * Features:
 * - Display current selected date
 * - Previous/Next day navigation buttons
 * - Clean, simple UI
 *
 * @param selectedDate The currently selected date
 * @param onDateChanged Callback when user changes the date
 * @param modifier Optional modifier for customization
 */
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous day button
        Button(
            onClick = {
                val previousDay = selectedDate.minusDays(1)
                onDateChanged(previousDay)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("← Prev")
        }

        // Current date display
        Text(
            text = selectedDate.format(LocalDate.Formats.ISO),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        // Next day button
        Button(
            onClick = {
                val nextDay = selectedDate.plusDays(1)
                onDateChanged(nextDay)
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Next →")
        }
    }
}

