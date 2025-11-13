package com.hafnium.expense.data.image

/**
 * Interface for image storage operations.
 *
 * Provides platform-independent abstraction for storing and retrieving images.
 */
interface ImageStorage {
    
    /**
     * Save image data to storage and return the path.
     *
     * @param imageData The image bytes to store
     * @param filename Optional filename; if not provided, a unique name will be generated
     * @return Path to the stored image, or null if storage failed
     */
    suspend fun saveImage(imageData: ByteArray, filename: String = ""): String?
    
    /**
     * Delete image at the specified path.
     *
     * @param imagePath Path to the image to delete
     * @return True if deletion was successful, false otherwise
     */
    suspend fun deleteImage(imagePath: String): Boolean
    
    /**
     * Check if image exists at the specified path.
     *
     * @param imagePath Path to check
     * @return True if image exists, false otherwise
     */
    suspend fun imageExists(imagePath: String): Boolean
}

