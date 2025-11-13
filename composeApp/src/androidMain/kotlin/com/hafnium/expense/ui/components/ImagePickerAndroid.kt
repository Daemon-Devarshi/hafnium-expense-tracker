package com.hafnium.expense.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android implementation of rememberImagePicker.
 *
 * Uses ActivityResultContracts for modern image picking.
 */
@Composable
actual fun rememberImagePicker(
    onImageSelected: (ByteArray) -> Unit,
    onCancelled: () -> Unit
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                val bytes = context.contentResolver.openInputStream(it)?.readBytes()
                if (bytes != null) {
                    onImageSelected(bytes)
                } else {
                    onCancelled()
                }
            } else {
                onCancelled()
            }
        }
    )

    return remember { {
        launcher.launch("image/*")
    } }
}
