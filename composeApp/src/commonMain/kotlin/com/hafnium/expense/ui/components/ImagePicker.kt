package com.hafnium.expense.ui.components

expect class ImagePicker() {
    fun launchPicker(
        onImageSelected: (ByteArray) -> Unit,
        onCancelled: () -> Unit
    )
}
