package mr.liks.feature.search.impl.presentation

import androidx.paging.PagingData
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.StringProvider
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.model.SearchHistoryItem
import mr.liks.feature.search.impl.R
import mr.liks.feature.search.impl.domain.usecase.DeleteSearchHistoryItemUseCase
import mr.liks.feature.search.impl.domain.usecase.GetSearchHistoryUseCase
import mr.liks.feature.search.impl.domain.usecase.SaveSearchQueryUseCase
import mr.liks.feature.search.impl.domain.usecase.SearchGamesUseCase
import mr.liks.feature.search.impl.testUtil.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchGames = mockk<SearchGamesUseCase>()
    private val getSearchHistory = mockk<GetSearchHistoryUseCase>()
    private val saveSearchQuery = mockk<SaveSearchQueryUseCase>(relaxed = true)
    private val deleteSearchHistoryItem = mockk<DeleteSearchHistoryItemUseCase>(relaxed = true)
    private val stringProvider = mockk<StringProvider>()
    private val logger = mockk<AppLogger>(relaxed = true)

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        every { getSearchHistory.invoke() } returns flowOf(emptyList())
        every { searchGames.invoke(any()) } returns flowOf(PagingData.empty())
        every { stringProvider.getString(R.string.delete_search_record_error) } returns "delete error"

        viewModel = createViewModel()
    }

    private fun createViewModel() = SearchViewModel(
        searchGames = searchGames,
        getSearchHistory = getSearchHistory,
        saveSearchQuery = saveSearchQuery,
        deleteSearchHistoryItem = deleteSearchHistoryItem,
        stringProvider = stringProvider,
        logger = logger
    )

    @Test
    fun `initial state`() = runTest {
        assertEquals(SearchUiState(), viewModel.uiState.value)
    }

    @Test
    fun `observeHistory updates state`() = runTest {
        val item = SearchHistoryItem(id = 1L, query = "query", searchedAt = 123L)
        every { getSearchHistory.invoke() } returns flowOf(listOf(item))

        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(listOf(item), viewModel.uiState.value.history)
    }

    @Test
    fun `QueryChanged updates query and hides history`() = runTest {
        viewModel.onIntent(SearchIntent.QueryChanged("game"))

        assertEquals("game", viewModel.uiState.value.query)
        assertFalse(viewModel.uiState.value.showHistory)
    }

    @Test
    fun `QueryChanged blank shows history`() = runTest {
        viewModel.onIntent(SearchIntent.QueryChanged("game"))
        viewModel.onIntent(SearchIntent.QueryChanged(""))

        assertTrue(viewModel.uiState.value.showHistory)
    }

    @Test
    fun `Submit saves non blank query`() = runTest {
        viewModel.onIntent(SearchIntent.QueryChanged("game"))
        viewModel.onIntent(SearchIntent.Submit)
        advanceUntilIdle()

        coVerify { saveSearchQuery.invoke("game") }
    }

    @Test
    fun `Submit blank does nothing`() = runTest {
        viewModel.onIntent(SearchIntent.Submit)
        advanceUntilIdle()

        coVerify(exactly = 0) { saveSearchQuery.invoke(any()) }
    }

    @Test
    fun `HistoryItemClicked sets query and saves`() = runTest {
        viewModel.onIntent(SearchIntent.HistoryItemClicked("history"))
        advanceUntilIdle()

        assertEquals("history", viewModel.uiState.value.query)
        assertFalse(viewModel.uiState.value.showHistory)
        coVerify { saveSearchQuery.invoke("history") }
    }

    @Test
    fun `DeleteHistoryItem success`() = runTest {
        viewModel.onIntent(SearchIntent.DeleteHistoryItem("query"))
        advanceUntilIdle()

        coVerify { deleteSearchHistoryItem.invoke("query") }
    }

    @Test
    fun `DeleteHistoryItem failure sets error`() = runTest {
        coEvery { deleteSearchHistoryItem.invoke("query") } throws RuntimeException("fail")

        viewModel.onIntent(SearchIntent.DeleteHistoryItem("query"))
        advanceUntilIdle()

        assertEquals("fail", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `DeleteHistoryItem failure without message uses fallback`() = runTest {
        coEvery { deleteSearchHistoryItem.invoke("query") } throws RuntimeException()

        viewModel.onIntent(SearchIntent.DeleteHistoryItem("query"))
        advanceUntilIdle()

        assertEquals("delete error", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `GameClicked emits NavigateToDetails`() = runTest {
        viewModel.effects.test {
            viewModel.onIntent(SearchIntent.GameClicked(42))
            assertEquals(SearchEffect.NavigateToDetails(42), awaitItem())
        }
    }

    @Test
    fun `ClearQuery clears query`() = runTest {
        viewModel.onIntent(SearchIntent.QueryChanged("game"))
        viewModel.onIntent(SearchIntent.ClearQuery)

        assertEquals("", viewModel.uiState.value.query)
        assertTrue(viewModel.uiState.value.showHistory)
    }

    @Test
    fun `DismissError clears error`() = runTest {
        coEvery { deleteSearchHistoryItem.invoke("query") } throws RuntimeException("fail")
        viewModel.onIntent(SearchIntent.DeleteHistoryItem("query"))
        advanceUntilIdle()

        viewModel.onIntent(SearchIntent.DismissError)

        assertNull(viewModel.uiState.value.errorMessage)
    }
}