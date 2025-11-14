package com.hafnium.expense.ui.components

/**
 * Android implementation of ImagePicker.
 *
 * Uses Activity Result API for selecting photos from the gallery.
 * Note: Full implementation requires Activity context and launcher setup.
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
        // Note: In a real implementation, this would integrate with Activity Result API
        // For now, this is a placeholder
    }
}
