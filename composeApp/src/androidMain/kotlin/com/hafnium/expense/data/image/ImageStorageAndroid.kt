package com.hafnium.expense.data.image

import android.content.Context
import java.io.File
import java.util.UUID

/**
 * Android-specific implementation of ImageStorage.
 *
 * Stores images in the app's cache directory.
 */
class ImageStorageAndroid(private val context: Context) : ImageStorage {
    
    private val imageDir: File
        get() = File(context.cacheDir, "images").apply {
            mkdirs()
        }
    
    override suspend fun saveImage(imageData: ByteArray, filename: String): String? {
        return try {
            val name = if (filename.isNotEmpty()) filename else "${UUID.randomUUID()}.jpg"
            val file = File(imageDir, name)
            file.writeBytes(imageData)
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun deleteImage(imagePath: String): Boolean {
        return try {
            File(imagePath).delete()
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun imageExists(imagePath: String): Boolean {
        return try {
            File(imagePath).exists()
        } catch (e: Exception) {
            false
        }
    }
}

