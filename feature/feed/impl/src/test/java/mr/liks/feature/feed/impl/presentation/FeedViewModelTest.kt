package mr.liks.feature.feed.impl.presentation

import androidx.paging.PagingData
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.feature.feed.impl.domain.usecase.GetFeedPagingDataUseCase
import mr.liks.feature.feed.impl.domain.usecase.RefreshFeedUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val getFeedPagingData: GetFeedPagingDataUseCase = mockk()
    private val refreshFeed: RefreshFeedUseCase = mockk()
    private val logger: AppLogger = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getFeedPagingData() } returns flowOf(PagingData.empty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)
        val state = viewModel.uiState.value
        assertEquals(false, state.isRefreshing)
        assertNull(state.errorMessage)
    }

    @Test
    fun `Refresh intent sets isRefreshing then clears on success`() = runTest {
        coEvery { refreshFeed() } returns Unit
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)

        viewModel.uiState.test {
            assertEquals(false, awaitItem().isRefreshing)
            viewModel.onIntent(FeedIntent.Refresh)
            assertEquals(true, awaitItem().isRefreshing)
            assertEquals(false, awaitItem().isRefreshing)
        }
        coVerify(exactly = 1) { refreshFeed() }
    }

    @Test
    fun `Refresh intent sets error message on failure`() = runTest {
        val error = RuntimeException("Network error")
        coEvery { refreshFeed() } throws error
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)

        viewModel.uiState.test {
            assertEquals(false, awaitItem().isRefreshing)
            viewModel.onIntent(FeedIntent.Refresh)
            assertEquals(true, awaitItem().isRefreshing)
            val errorState = awaitItem()
            assertEquals(false, errorState.isRefreshing)
            assertEquals("Network error", errorState.errorMessage)
        }
    }

    @Test
    fun `GameClicked sends NavigateToDetails effect`() = runTest {
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)

        viewModel.effects.test {
            viewModel.onIntent(FeedIntent.GameClicked(123L))
            assertEquals(FeedEffect.NavigateToDetails(123L), awaitItem())
        }
    }

    @Test
    fun `DismissError clears error message`() = runTest {
        coEvery { refreshFeed() } throws RuntimeException("Error")
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)
        viewModel.onIntent(FeedIntent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(FeedIntent.DismissError)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `Retry intent calls refresh`() = runTest {
        coEvery { refreshFeed() } returns Unit
        val viewModel = FeedViewModel(getFeedPagingData, refreshFeed, logger)
        viewModel.onIntent(FeedIntent.Retry)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify(exactly = 1) { refreshFeed() }
    }
}