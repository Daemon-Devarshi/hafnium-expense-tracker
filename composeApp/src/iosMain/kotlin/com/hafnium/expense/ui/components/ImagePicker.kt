package com.hafnium.expense.ui.components

/**
 * iOS implementation of ImagePicker.
 *
 * Uses UIImagePickerController for selecting photos from the photo library.
 * Note: Full implementation requires UIKit integration at the composition level.
 */
actual class ImagePicker actual constructor() {
    private var onImageSelectedCallback: ((ByteArray) -> Unit)? = null
    private var onCancelledCallback: (() -> Unit)? = null

    actual fun launchPicker(
        onImageSelected: (ByteArray) -> Unit,
        onCancelled: () -> Unit
    ) {
        onImageSelectedCallback = onImageSelected
        onCancelledCallback = onCancelled
        // Note: In a real implementation, this would integrate with UIImagePickerController
        // For now, this is a placeholder
    }
}
