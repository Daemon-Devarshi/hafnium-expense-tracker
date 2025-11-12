package com.hafnium.expense.data.image

import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import java.util.UUID

/**
 * iOS-specific implementation of ImageStorage.
 *
 * Stores images in the app's cache directory.
 */
class ImageStorageIos : ImageStorage {

    private val imageDir: String
        get() {
            val paths = NSSearchPathForDirectoriesInDomains(
                NSCachesDirectory,
                NSUserDomainMask,
                true
            )
            val cachePath = (paths.firstOrNull() as? String) ?: ""
            val imagePath = "$cachePath/images"
            NSFileManager.defaultManager.createDirectoryAtPath(
                imagePath,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
            return imagePath
        }

    override suspend fun saveImage(imageData: ByteArray, filename: String): String? {
        return try {
            val name = if (filename.isNotEmpty()) filename else "${UUID.randomUUID()}.jpg"
            val path = "$imageDir/$name"
            NSFileManager.defaultManager.createFileAtPath(
                path,
                contents = imageData.toNSData(),
                attributes = null
            )
            path
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteImage(imagePath: String): Boolean {
        return try {
            NSFileManager.defaultManager.removeItemAtPath(imagePath, error = null)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun imageExists(imagePath: String): Boolean {
        return try {
            NSFileManager.defaultManager.fileExistsAtPath(imagePath)
        } catch (e: Exception) {
            false
        }
    }

    private fun ByteArray.toNSData(): Any {
        // Convert ByteArray to NSData
        @Suppress("UNCHECKED_CAST")
        return with(this) {
            platform.Foundation.NSData(
                bytes = this as? Any,
                length = this.size.toULong()
            )
        }
    }
}

