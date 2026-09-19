package mr.liks.core.common

/** Контракт управления дисковым кешем */
interface CacheManager {
    /** Размер дискового кеша в байтах (видео + изображения + временные файлы) */
    suspend fun getCacheSizeBytes(): Long

    /** Полностью очищает дисковый кеш */
    suspend fun clearAll()

    /** Очищает только кеш изображений (Coil) */
    suspend fun clearImages()

    /** Очищает только кеш видео (ExoPlayer SimpleCache) */
    suspend fun clearVideo()
}