package mr.liks.core.media

import android.content.Context
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mr.liks.core.common.CacheManager
import java.io.File

/** Реализация [CacheManager] */
@UnstableApi
class CacheManagerImpl(
    private val context: Context
) : CacheManager {
    override suspend fun getCacheSizeBytes(): Long = withContext(Dispatchers.IO) {
        val video = VideoCache.get(context).cacheSpace
        val images = imageCacheDir()?.let { dirSize(it) } ?: 0L
        video + images
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        runCatching {
            VideoCache.get(context).keys.toList().forEach { key ->
                VideoCache.get(context).removeResource(key)
            }
        }
        imageCacheDir()?.deleteRecursively()
        Unit
    }

    override suspend fun clearImages() = withContext(Dispatchers.IO) {
        imageCacheDir()?.deleteRecursively()
        Unit
    }

    override suspend fun clearVideo() = withContext(Dispatchers.IO) {
        val cache = VideoCache.get(context)
        cache.keys.toList().forEach { key -> cache.removeResource(key) }
    }

    private fun imageCacheDir(): File? {
        return context.cacheDir.resolve("image_cache").takeIf { it.exists() }
    }

    private fun dirSize(dir: File): Long {
        if (!dir.exists()) return 0L
        return dir.walkBottomUp()
            .filter { it.isFile }
            .sumOf { it.length() }
    }
}