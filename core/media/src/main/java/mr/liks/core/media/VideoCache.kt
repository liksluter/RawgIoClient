package mr.liks.core.media

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

/** Дисковый кеш видео на базе ExoPlayer [SimpleCache] */
@UnstableApi
object VideoCache {
    @Volatile
    private var instance: SimpleCache? = null

    /** Возвращает синглтон инстанс [SimpleCache] */
    fun get(context: Context): SimpleCache {
        return instance ?: synchronized(this) {
            instance ?: build(context).also { instance = it }
        }
    }

    /** Фабрика источников данных, читает и пишет кеш */
    fun cacheDataSourceFactory(context: Context): DataSource.Factory {
        val upstream = DefaultDataSource.Factory(context)
        return CacheDataSource.Factory()
            .setCache(get(context))
            .setUpstreamDataSourceFactory(upstream)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun build(context: Context): SimpleCache {
        val dir = File(context.cacheDir, CACHE_DIR_NAME)
        val evictor = LeastRecentlyUsedCacheEvictor(DEFAULT_MAX_BYTES)
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(dir, evictor, databaseProvider)
    }

    private const val CACHE_DIR_NAME = "video_cache"
    private const val DEFAULT_MAX_BYTES = 200L * 1024 * 1024
}