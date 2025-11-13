package com.hafnium.expense.ui.components

import androidx.compose.runtime.Composable

/**
 * Creates and remembers a launcher for picking an image from the device's gallery.
 *
 * This is a composable function that should be called within a Composable context.
 *
 * @param onImageSelected A callback that will be invoked with the selected image's data as a ByteArray.
 * @param onCancelled A callback that will be invoked if the user cancels the image selection.
 * @return A lambda function that, when called, will launch the image picker.
 */
@Composable
expect fun rememberImagePicker(
    onImageSelected: (ByteArray) -> Unit,
    onCancelled: () -> Unit
): () -> Unit
