package mr.liks.feature.feed.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.github.takahirom.roborazzi.captureRoboImage
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.flow.flowOf
import mr.liks.core.model.GamePreview
import mr.liks.core.model.PlatformIcon
import mr.liks.feature.feed.impl.presentation.component.GameFeedCard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xhdpi")
class FeedScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val sampleGame = GamePreview(
        id = 1L,
        name = "The Witcher 3: Wild Hunt",
        backgroundImage = "https://example.com/witcher.jpg",
        rating = 4.8,
        platforms = listOf(
            PlatformIcon(1L, "PC", "pc.png"),
            PlatformIcon(2L, "PlayStation 5", "ps5.png"),
            PlatformIcon(3L, "Xbox Series X", "xbox.png")
        ),
        platformsNames = "PC, PlayStation 5, Xbox Series X",
        feedOrder = 0L,
        trailerUrl = null,
        trailerPreview = null
    )

    private val secondGame = GamePreview(
        id = 2L,
        name = "Cyberpunk 2077",
        backgroundImage = "https://example.com/cyberpunk.jpg",
        rating = 4.2,
        platforms = listOf(PlatformIcon(1L, "PC", "pc.png")),
        platformsNames = "PC",
        feedOrder = 1L,
        trailerUrl = null,
        trailerPreview = null
    )

    private val gameWithoutPlatforms = sampleGame.copy(
        platforms = emptyList(),
        platformsNames = null,
        rating = 0.0
    )

    @Test
    fun gameFeedCard_default() {
        composeRule.setContent {
            GameFeedCard(game = sampleGame, isVisible = true, onClick = {})
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/game_feed_card_default.png"
        )
    }

    @Test
    fun gameFeedCard_withoutPlatformsAndRating() {
        composeRule.setContent {
            GameFeedCard(game = gameWithoutPlatforms, isVisible = true, onClick = {})
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/game_feed_card_no_platforms.png"
        )
    }

    @Test
    fun feedContent_loading() {
        composeRule.setContent {
            TestFeedContent(
                pagingData = pagingData(
                    items = emptyList(),
                    refresh = LoadState.Loading
                )
            )
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/feed_content_loading.png"
        )
    }

    @Test
    fun feedContent_error() {
        composeRule.setContent {
            TestFeedContent(
                pagingData = pagingData(
                    items = emptyList(),
                    refresh = LoadState.Error(RuntimeException("Network error"))
                )
            )
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/feed_content_error.png"
        )
    }

    @Test
    fun feedContent_empty() {
        composeRule.setContent {
            TestFeedContent(pagingData = pagingData(items = emptyList()))
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/feed_content_empty.png"
        )
    }

    @Test
    fun feedContent_withItems() {
        composeRule.setContent {
            TestFeedContent(
                pagingData = pagingData(items = listOf(sampleGame, secondGame))
            )
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/feed_content_items.png"
        )
    }

    @Test
    fun feedContent_withErrorMessageOverlay() {
        composeRule.setContent {
            TestFeedContent(
                pagingData = pagingData(items = listOf(sampleGame)),
                uiState = FeedUiState(errorMessage = "Не удалось обновить ленту")
            )
        }
        composeRule.waitForIdle()
        composeRule.onRoot().captureRoboImage(
            "build/screenshots/feed_content_with_error_overlay.png"
        )
    }

    @Composable
    private fun TestFeedContent(
        pagingData: PagingData<GamePreview>,
        uiState: FeedUiState = FeedUiState(),
        onGameClick: (Long) -> Unit = {}
    ) {
        val games: LazyPagingItems<GamePreview> =
            remember(pagingData) { flowOf(pagingData) }.collectAsLazyPagingItems()

        FeedContent(
            games = games,
            uiState = uiState,
            hazeState = HazeState(),
            listState = LazyListState(),
            contentPadding = PaddingValues(),
            onRefresh = {},
            onRetry = {},
            onGameClick = onGameClick
        )
    }

    private fun pagingData(
        items: List<GamePreview>,
        refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
    ): PagingData<GamePreview> = PagingData.from(
        data = items,
        sourceLoadStates = LoadStates(
            refresh = refresh,
            prepend = LoadState.NotLoading(endOfPaginationReached = true),
            append = LoadState.NotLoading(endOfPaginationReached = true)
        )
    )
}