package mr.liks.feature.feed.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.flowOf
import mr.liks.core.model.GamePreview
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xhdpi")
class FeedContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeGame = GamePreview(
        id = 1L,
        name = "Game 1",
        backgroundImage = "bg",
        rating = 4.0,
        platforms = emptyList(),
        platformsNames = "PC",
        feedOrder = 0L,
        trailerUrl = null,
        trailerPreview = null
    )

    @Test
    fun `shows placeholder when loading`() {
        val pagingData = PagingData.from(
            data = emptyList<GamePreview>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        composeRule.setContent {
            val games = flowOf(pagingData).collectAsLazyPagingItems()
            FeedContent(
                games = games,
                uiState = FeedUiState(),
                hazeState = HazeState(),
                listState = LazyListState(),
                contentPadding = PaddingValues(),
                onRefresh = {},
                onRetry = {},
                onGameClick = {}
            )
        }
        composeRule.waitForIdle()
    }

    @Test
    fun `shows error when refresh fails`() {
        val pagingData = PagingData.from(
            data = emptyList<GamePreview>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(RuntimeException("Error")),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        composeRule.setContent {
            val games = flowOf(pagingData).collectAsLazyPagingItems()
            FeedContent(
                games = games,
                uiState = FeedUiState(),
                hazeState = HazeState(),
                listState = LazyListState(),
                contentPadding = PaddingValues(),
                onRefresh = {},
                onRetry = {},
                onGameClick = {}
            )
        }
        composeRule.waitForIdle()
    }

    @Test
    fun `shows empty when no items`() {
        val pagingData = PagingData.from(
            data = emptyList<GamePreview>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        composeRule.setContent {
            val games = flowOf(pagingData).collectAsLazyPagingItems()
            FeedContent(
                games = games,
                uiState = FeedUiState(),
                hazeState = HazeState(),
                listState = LazyListState(),
                contentPadding = PaddingValues(),
                onRefresh = {},
                onRetry = {},
                onGameClick = {}
            )
        }
        composeRule.waitForIdle()
    }

    @Test
    fun `shows list when items present`() {
        val pagingData = PagingData.from(
            data = listOf(fakeGame),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        composeRule.setContent {
            val games = flowOf(pagingData).collectAsLazyPagingItems()
            FeedContent(
                games = games,
                uiState = FeedUiState(),
                hazeState = HazeState(),
                listState = LazyListState(),
                contentPadding = PaddingValues(),
                onRefresh = {},
                onRetry = {},
                onGameClick = {}
            )
        }
        composeRule.waitForIdle()
    }
}