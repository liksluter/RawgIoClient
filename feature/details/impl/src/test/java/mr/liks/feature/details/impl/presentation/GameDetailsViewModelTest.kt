package mr.liks.feature.details.impl.presentation

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mr.liks.core.common.applocagger.AppLogger
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
import org.junit.Assert.assertTrue
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

    private lateinit var viewModel: GameDetailsViewModel

    @Before
    fun setUp() {
        viewModel = GameDetailsViewModel(
            getGameDetails = getGameDetails,
            getGameMedia = getGameMedia,
            refreshDetails = refreshDetails,
            refreshMedia = refreshMedia,
            logger = logger
        )
    }

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
    fun `OpenMedia and CloseMedia update selectedMediaIndex`() {
        viewModel.onIntent(GameDetailsIntent.OpenMedia(2))
        assertEquals(2, viewModel.uiState.value.selectedMediaIndex)

        viewModel.onIntent(GameDetailsIntent.CloseMedia)
        assertNull(viewModel.uiState.value.selectedMediaIndex)
    }

    @Test
    fun `DismissError clears error message`() {
        viewModel.onIntent(GameDetailsIntent.DismissError)

        assertNull(viewModel.uiState.value.errorMessage)
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