package mr.liks.feature.settings.impl.data

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.CacheManager
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode
import mr.liks.datastore.SettingsDataStore
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {
    private val dataStore: SettingsDataStore = mockk(relaxed = true)
    private val cacheManager: CacheManager = mockk(relaxed = true)

    @Test
    fun `observeSettings combines datastore flow and cache size`() = runTest {
        val repository = createRepository()

        val settingsFlow = MutableStateFlow(AppSettings(themeMode = ThemeMode.Light))
        every { dataStore.flow } returns settingsFlow
        coEvery { cacheManager.getCacheSizeBytes() } returns 1024L

        repository.observeSettings().test {
            val first = awaitItem()
            assertEquals(ThemeMode.Light, first.themeMode)
            assertEquals(1024L, first.cacheSizeBytes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setTheme calls datastore setThemeMode`() = runTest {
        val repository = createRepository()

        repository.setTheme(ThemeMode.Dark)
        coVerify { dataStore.setThemeMode(ThemeMode.Dark) }
    }

    @Test
    fun `setDynamicColor calls datastore setDynamicColor`() = runTest {
        val repository = createRepository()

        repository.setDynamicColor(true)
        coVerify { dataStore.setDynamicColor(true) }
    }

    @Test
    fun `getCacheSizeBytes returns cacheManager value`() = runTest {
        val repository = createRepository()

        coEvery { cacheManager.getCacheSizeBytes() } returns 2048L
        assertEquals(2048L, repository.getCacheSizeBytes())
    }

    @Test
    fun `clearCache calls cacheManager clearAll`() = runTest {
        val repository = createRepository()

        repository.clearCache()
        coVerify { cacheManager.clearAll() }
    }

    @Test
    fun `cacheSizeFlow emits updates every 5 seconds`() = runTest {
        val repository = createRepository()

        val settingsFlow = MutableStateFlow(AppSettings())
        every { dataStore.flow } returns settingsFlow
        var size = 100L
        coEvery { cacheManager.getCacheSizeBytes() } answers { size }

        repository.observeSettings().test {
            assertEquals(100L, awaitItem().cacheSizeBytes)
            size = 200L
            advanceTimeBy(5.seconds)
            assertEquals(200L, awaitItem().cacheSizeBytes)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun TestScope.createRepository(): SettingsRepositoryImpl {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = mockk<DispatchersProvider> {
            every { io } returns testDispatcher
        }
        return SettingsRepositoryImpl(
            dataStore = dataStore,
            cacheManager = cacheManager,
            dispatchers = dispatchers,
            externalScope = CoroutineScope(testDispatcher)
        )
    }
}