package mr.liks.feature.settings.impl.presentation

import android.os.Build
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.usecase.ClearCacheUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetAppSettingsUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetCacheSizeUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetDynamicColorUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetThemeUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    private val getAppSettings: GetAppSettingsUseCase = mockk()
    private val setTheme: SetThemeUseCase = mockk(relaxed = true)
    private val setDynamicColor: SetDynamicColorUseCase = mockk(relaxed = true)
    private val getCacheSize: GetCacheSizeUseCase = mockk()
    private val clearCache: ClearCacheUseCase = mockk(relaxed = true)
    private val logger: AppLogger = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getAppSettings() } returns flowOf(AppSettings())
        coEvery { getCacheSize() } returns 0L
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel = SettingsViewModel(
            getAppSettings = getAppSettings,
            setTheme = setTheme,
            setDynamicColor = setDynamicColor,
            getCacheSize = getCacheSize,
            clearCache = clearCache,
            appVersion = "1.0.0",
            logger = logger
        )
    }

    @Test
    fun `initial state has correct defaults`() = runTest {
        createViewModel()
        val state = viewModel.uiState.value
        assertEquals("1.0.0", state.appVersion)
        assertEquals(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S, state.supportsDynamicColor)
        assertFalse(state.isClearingCache)
        assertNull(state.errorMessage)
    }

    @Test
    fun `observeSettings updates uiState`() = runTest {
        val settings = AppSettings(themeMode = ThemeMode.Dark, dynamicColor = true, cacheSizeBytes = 500L)
        every { getAppSettings() } returns MutableStateFlow(settings)

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(ThemeMode.Dark, viewModel.uiState.value.themeMode)
        assertTrue(viewModel.uiState.value.dynamicColor)
        assertEquals(500L, viewModel.uiState.value.cacheSizeBytes)
    }

    @Test
    fun `onIntent SetTheme calls setTheme use case`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.SetTheme(ThemeMode.Light))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { setTheme(ThemeMode.Light) }
    }

    @Test
    fun `onIntent SetTheme handles error`() = runTest {
        coEvery { setTheme(any()) } throws RuntimeException("Test error")
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.SetTheme(ThemeMode.Light))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Test error", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onIntent SetDynamicColor calls setDynamicColor use case`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.SetDynamicColor(true))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { setDynamicColor(true) }
    }

    @Test
    fun `onIntent ClearCache clears cache and updates size`() = runTest {
        val clearStarted = CompletableDeferred<Unit>()
        val clearGate = CompletableDeferred<Unit>()

        coEvery { clearCache() } coAnswers {
            clearStarted.complete(Unit)
            clearGate.await()
        }
        coEvery { getCacheSize() } returns 0L

        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.ClearCache)

        clearStarted.await()
        testDispatcher.scheduler.runCurrent()

        assertTrue(viewModel.uiState.value.isClearingCache)

        clearGate.complete(Unit)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { clearCache() }
        assertFalse(viewModel.uiState.value.isClearingCache)
        assertEquals(0L, viewModel.uiState.value.cacheSizeBytes)
    }

    @Test
    fun `onIntent ClearCache handles error`() = runTest {
        coEvery { clearCache() } throws RuntimeException("Clear error")
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.ClearCache)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isClearingCache)
        assertEquals("Clear error", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onIntent DismissError clears error`() = runTest {
        coEvery { setTheme(any()) } throws RuntimeException("Error")
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.SetTheme(ThemeMode.Dark))
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Error", viewModel.uiState.value.errorMessage)

        viewModel.onIntent(SettingsIntent.DismissError)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onIntent OpenUrl sends effect`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            viewModel.onIntent(SettingsIntent.OpenUrl("https://example.com"))
            assertEquals(SettingsEffect.OpenUrl("https://example.com"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearCache success sends snackbar effect`() = runTest {
        createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            viewModel.onIntent(SettingsIntent.ClearCache)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(SettingsEffect.ShowSnackbar("Кеш очищен"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}