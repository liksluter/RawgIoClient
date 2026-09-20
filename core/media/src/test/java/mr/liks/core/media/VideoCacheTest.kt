package mr.liks.core.media

import android.content.Context
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cache.SimpleCache
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.RuntimeEnvironment
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

/** Тесты на [VideoCache] */
@ExtendWith(RobolectricExtension::class)
class VideoCacheTest {

    private lateinit var context: Context

    @BeforeEach
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        resetSingleton()
    }

    @AfterEach
    fun tearDown() {
        singletonInstance()?.release()
        resetSingleton()
    }

    @Test
    fun `get returns same instance`() {
        val cache1 = VideoCache.get(context)
        val cache2 = VideoCache.get(context)
        assertSame(cache1, cache2)
    }

    @Test
    fun `get creates SimpleCache`() {
        val cache = VideoCache.get(context)
        assertNotNull(cache)
        assertTrue(cache is SimpleCache)
    }

    @Test
    fun `cacheDataSourceFactory returns factory`() {
        val factory: DataSource.Factory = VideoCache.cacheDataSourceFactory(context)
        assertNotNull(factory)
        assertTrue(factory is DataSource.Factory)
    }

    @Test
    fun `cacheDataSourceFactory uses the same cache instance`() {
        VideoCache.cacheDataSourceFactory(context)
        assertNotNull(VideoCache.get(context))
    }

    private fun singletonInstance(): SimpleCache? =
        VideoCache::class.java
            .getDeclaredField("instance")
            .apply { isAccessible = true }
            .get(null) as? SimpleCache

    private fun resetSingleton() {
        VideoCache::class.java
            .getDeclaredField("instance")
            .apply { isAccessible = true }
            .set(null, null)
    }
}