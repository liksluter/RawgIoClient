package mr.liks.core.media

import android.content.Context
import androidx.media3.datasource.cache.SimpleCache
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/** Тесты на [CacheManagerImpl] */
class CacheManagerImplTest {
    private lateinit var context: Context
    private lateinit var cacheManager: CacheManagerImpl

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        context = mockk(relaxed = true)
        cacheManager = CacheManagerImpl(context)
        mockkObject(VideoCache)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCacheSizeBytes returns sum of video and image cache`() = runTest {
        val videoCacheSize = 1000L
        val imageCacheSize = 500L
        val imageCacheDir = File(tempDir, "image_cache").apply { mkdirs() }
        File(imageCacheDir, "img1.jpg").writeBytes(ByteArray(imageCacheSize.toInt()))

        every { VideoCache.get(context) } returns mockk(relaxed = true) {
            every { cacheSpace } returns videoCacheSize
        }
        every { context.cacheDir } returns tempDir

        val size = cacheManager.getCacheSizeBytes()

        assertEquals(videoCacheSize + imageCacheSize, size)
    }

    @Test
    fun `clearAll clears video and image cache`() = runTest {
        val videoCache = mockk<SimpleCache>(relaxed = true)
        val keys = setOf("key1", "key2")
        every { VideoCache.get(context) } returns videoCache
        every { videoCache.keys } returns keys
        every { videoCache.removeResource(any()) } just runs

        val imageCacheDir = File(tempDir, "image_cache").apply { mkdirs() }
        File(imageCacheDir, "img1.jpg").writeText("data")
        every { context.cacheDir } returns tempDir

        cacheManager.clearAll()

        keys.forEach { key -> verify { videoCache.removeResource(key) } }
        assertFalse(imageCacheDir.exists())
    }

    @Test
    fun `clearImages deletes only image cache`() = runTest {
        val imageCacheDir = File(tempDir, "image_cache").apply { mkdirs() }
        File(imageCacheDir, "img1.jpg").writeText("data")
        every { context.cacheDir } returns tempDir

        cacheManager.clearImages()

        assertFalse(imageCacheDir.exists())
    }

    @Test
    fun `clearVideo removes all video cache resources`() = runTest {
        val videoCache = mockk<SimpleCache>(relaxed = true)
        val keys = setOf("key1", "key2")
        every { VideoCache.get(context) } returns videoCache
        every { videoCache.keys } returns keys
        every { videoCache.removeResource(any()) } just runs

        cacheManager.clearVideo()

        keys.forEach { key -> verify { videoCache.removeResource(key) } }
    }
}