package com.hafnium.expense.ui.components

/**
 * Platform-independent image picker interface.
 *
 * Implementations should be provided per platform:
 * - androidMain: ImagePickerAndroid.kt
 * - iosMain: ImagePickerIos.kt
 */
expect class ImagePicker {
    fun launchPicker(
        onImageSelected: (ByteArray) -> Unit,
        onCancelled: () -> Unit = {}
    )
}

