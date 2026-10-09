package mr.liks.feature.details.impl.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mr.liks.core.common.StringProvider
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.media.TrailerPlayerController
import mr.liks.core.model.GameDetails
import mr.liks.core.model.GameMedia
import mr.liks.core.model.Trailer
import mr.liks.feature.details.impl.domain.usecase.GetGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.GetGameMediaUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameMediaUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/** Тесты на [GameDetailsViewModel] */
@OptIn(ExperimentalCoroutinesApi::class)
class GameDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getGameDetails = mockk<GetGameDetailsUseCase>()
    private val getGameMedia = mockk<GetGameMediaUseCase>()
    private val refreshDetails = mockk<RefreshGameDetailsUseCase>()
    private val refreshMedia = mockk<RefreshGameMediaUseCase>()
    private val logger = mockk<AppLogger>(relaxed = true)
    private val trailerPlayerController = mockk<TrailerPlayerController>(relaxed = true)
    private val stringProvider: StringProvider = mockk(relaxed = true)

    private lateinit var viewModel: GameDetailsViewModel

    @Before
    fun setUp() {
        viewModel = createViewModel()
    }

    private fun createViewModel() = GameDetailsViewModel(
        getGameDetails = getGameDetails,
        getGameMedia = getGameMedia,
        refreshDetails = refreshDetails,
        refreshMedia = refreshMedia,
        stringProvider = stringProvider,
        trailerPlayerController = trailerPlayerController,
        logger = logger
    )

    @Test
    fun `loadGameDetails observes details and media`() = runTest {
        val details = mockk<GameDetails>(relaxed = true)
        val media = GameMedia(
            trailers = listOf(
                Trailer(
                    id = 1L,
                    name = "Trailer",
                    preview = "preview",
                    data480 = "480",
                    dataMax = "max"
                )
            ),
            screenshots = emptyList()
        )

        every { getGameDetails(1L) } returns flowOf(details)
        every { getGameMedia(1L) } returns flowOf(media)
        coEvery { refreshDetails(1L) } just runs
        coEvery { refreshMedia(1L) } just runs

        viewModel.loadGameDetails(1L)
        advanceUntilIdle()

        assertEquals(details, viewModel.uiState.value.details)
        assertEquals(media, viewModel.uiState.value.media)
        assertFalse(viewModel.uiState.value.isLoadingDetails)
        assertFalse(viewModel.uiState.value.isLoadingMedia)
    }

    @Test
    fun `loadGameDetails with same gameId does not reset state`() = runTest {
        val details = mockk<GameDetails>(relaxed = true)
        every { getGameDetails(1L) } returns flowOf(details)
        every { getGameMedia(1L) } returns flowOf(
            GameMedia(trailers = emptyList(), screenshots = emptyList())
        )
        coEvery { refreshDetails(1L) } just runs
        coEvery { refreshMedia(1L) } just runs

        viewModel.loadGameDetails(1L)
        advanceUntilIdle()
        viewModel.onIntent(GameDetailsIntent.OpenMedia(3))

        viewModel.loadGameDetails(1L)
        advanceUntilIdle()

        assertEquals(3, viewModel.uiState.value.selectedMediaIndex)
        assertEquals(details, viewModel.uiState.value.details)

        verify(exactly = 1) { getGameDetails(1L) }
        verify(exactly = 1) { getGameMedia(1L) }
    }

    @Test
    fun `refresh failure updates error and emits snackbar`() = runTest {
        every { getGameDetails(1L) } returns flowOf(null)
        every { getGameMedia(1L) } returns flowOf(
            GameMedia(trailers = emptyList(), screenshots = emptyList())
        )
        coEvery { refreshDetails(1L) } throws RuntimeException("boom")
        coEvery { refreshMedia(1L) } just runs

        viewModel.effects.test {
            viewModel.loadGameDetails(1L)
            advanceUntilIdle()

            assertEquals("boom", viewModel.uiState.value.errorMessage)
            assertEquals(
                GameDetailsEffect.ShowSnackbar("boom"),
                awaitItem()
            )
        }
    }

    @Test
    fun `OpenMedia updates selectedMediaIndex`() {
        viewModel.onIntent(GameDetailsIntent.OpenMedia(2))
        assertEquals(2, viewModel.uiState.value.selectedMediaIndex)
    }

    @Test
    fun `CloseMedia clears selection and pauses player`() {
        viewModel.onIntent(GameDetailsIntent.OpenMedia(2))

        viewModel.onIntent(GameDetailsIntent.CloseMedia)

        assertNull(viewModel.uiState.value.selectedMediaIndex)
        verify { trailerPlayerController.pause() }
    }

    @Test
    fun `DismissError clears error message`() {
        viewModel.onIntent(GameDetailsIntent.DismissError)

        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `clearing viewModel releases player`() {
        val store = ViewModelStore()
        val factory = viewModelFactory {
            initializer {
                GameDetailsViewModel(
                    getGameDetails = getGameDetails,
                    getGameMedia = getGameMedia,
                    refreshDetails = refreshDetails,
                    refreshMedia = refreshMedia,
                    stringProvider = stringProvider,
                    trailerPlayerController = trailerPlayerController,
                    logger = logger
                )
            }
        }
        ViewModelProvider(store, factory)[GameDetailsViewModel::class.java]

        store.clear()

        verify { trailerPlayerController.release() }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: kotlinx.coroutines.test.TestDispatcher =
        UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}