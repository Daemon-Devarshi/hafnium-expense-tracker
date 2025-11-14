package com.hafnium.expense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.hafnium.expense.ui.components.DatePickerField
import com.hafnium.expense.ui.viewmodel.CaptureViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * Screen for capturing or editing a single expense.
 *
 * Features:
 * - Date picker (defaults to today)
 * - Amount input (positive integer only)
 * - Optional photo attachment
 * - Form validation with error display
 */
class CaptureScreen(private val expenseId: Long? = null) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: CaptureViewModel = koinInject(parameters = { parametersOf(expenseId) })
        val uiState by viewModel.uiState.collectAsState()

        // Handle events
        LaunchedEffect(uiState.event) {
            when (uiState.event) {
                is CaptureViewModel.UiEvent.SaveSuccess -> {
                    // Navigate back to list
                    navigator?.pop()
                }
                is CaptureViewModel.UiEvent.SaveError -> {
                    // Show error toast (would use a SnackbarHost in a real app)
                    println("Error: ${(uiState.event as CaptureViewModel.UiEvent.SaveError).message}")
                }
                is CaptureViewModel.UiEvent.LoadError -> {
                    println("Load error: ${(uiState.event as CaptureViewModel.UiEvent.LoadError).message}")
                }
                null -> {}
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (expenseId != null && expenseId > 0) "Edit Expense" else "Add Expense"
                        )
                    },
                    navigationIcon = {
                        Button(onClick = { navigator?.pop() }) {
                            Text("← Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date Picker
                DatePickerField(
                    selectedDate = uiState.selectedDate,
                    onDateSelected = viewModel::onDateChanged,
                    isError = uiState.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_DATE),
                    errorMessage = uiState.validationErrors[CaptureViewModel.ValidationError.INVALID_DATE]
                )

                // Amount Input
                OutlinedTextField(
                    value = uiState.amountText,
                    onValueChange = viewModel::onAmountChanged,
                    label = { Text("Amount") },
                    placeholder = { Text("Enter amount") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_AMOUNT),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true
                )

                // Error message for amount
                if (uiState.validationErrors.containsKey(CaptureViewModel.ValidationError.INVALID_AMOUNT)) {
                    Text(
                        text = uiState.validationErrors[CaptureViewModel.ValidationError.INVALID_AMOUNT] ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Description Input
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::onDescriptionChanged,
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("Enter notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                // Image Preview & Picker Placeholder
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            // TODO: Integrate image picker (T025-T027)
                        }
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageData = uiState.selectedImageData
                        when {
                            imageData != null -> {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📷", style = MaterialTheme.typography.headlineLarge)
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            "Image selected",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            "${imageData.size} bytes",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Button(
                                        onClick = viewModel::onPhotoClear,
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text(
                                            "Clear",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                            uiState.existingImagePath != null -> {
                                val existingImagePath = uiState.existingImagePath
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("📸", style = MaterialTheme.typography.headlineLarge)
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            "Photo stored",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            existingImagePath ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                            else -> {
                                Text(
                                    "📷 Tap to add a photo (optional)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Save Button
                Button(
                    onClick = viewModel::onSave,
                    enabled = !uiState.isSaving && uiState.amountText.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(if (uiState.isSaving) "Saving..." else "Save Expense")
                }
            }
        }
    }
}
