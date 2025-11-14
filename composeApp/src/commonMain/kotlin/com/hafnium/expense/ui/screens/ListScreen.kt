package com.hafnium.expense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.ui.components.DateSelector
import com.hafnium.expense.ui.viewmodel.ListViewModel
import org.koin.compose.koinInject

/**
 * Screen displaying a list of expenses for a selected date.
 *
 * Features:
 * - Display expenses for selected date (defaults to today)
 * - Date selector to switch between dates
 * - Tap an expense to edit it
 * - Delete an expense with confirmation
 * - FAB to add a new expense
 */
class ListScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: ListViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        // Handle events
        LaunchedEffect(uiState.event) {
            when (uiState.event) {
                is ListViewModel.UiEvent.DeleteSuccess -> {
                    // Show confirmation (would use SnackbarHost in production)
                    println("Expense deleted")
                }
                is ListViewModel.UiEvent.DeleteError -> {
                    println("Delete error: ${(uiState.event as ListViewModel.UiEvent.DeleteError).message}")
                }
                is ListViewModel.UiEvent.LoadError -> {
                    println("Load error: ${(uiState.event as ListViewModel.UiEvent.LoadError).message}")
                }
                null -> {}
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Expenses") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Navigate to CaptureScreen to add new expense
                        navigator?.push(CaptureScreen(expenseId = null))
                    }
                ) {
                    Text("➕", style = MaterialTheme.typography.headlineMedium)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Date Selector
                DateSelector(
                    selectedDate = uiState.selectedDate,
                    onDateChanged = { newDate ->
                        viewModel.onDateChanged(newDate)
                    }
                )

                // Expenses List
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading...")
                    }
                } else if (uiState.isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "No expenses for this date",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Button(onClick = {
                                navigator?.push(CaptureScreen(expenseId = null))
                            }) {
                                Text("Add an expense")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.expenses) { expense ->
                            ExpenseCard(
                                expense = expense,
                                onExpenseClick = {
                                    // Navigate to CaptureScreen to edit
                                    navigator?.push(CaptureScreen(expenseId = expense.id))
                                },
                                onDeleteClick = {
                                    viewModel.onExpenseDelete(expense.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable for a single expense card in the list.
 */
@Composable
private fun ExpenseCard(
    expense: Expense,
    onExpenseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onExpenseClick)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Amount and description
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "₹${expense.amount}",
                    style = MaterialTheme.typography.titleMedium
                )
                if (expense.description.isNotEmpty()) {
                    Text(
                        text = expense.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Photo indicator
            if (expense.imagePath != null) {
                Text("📸", style = MaterialTheme.typography.bodyLarge)
            }

            // Delete button
            Button(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Delete")
            }
        }
    }
}

