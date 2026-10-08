package mr.liks.feature.search.impl.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.paging.PagingData
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xhdpi")
class SearchScreenScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val viewModel = mockk<SearchViewModel>(relaxed = true)

    @Test
    fun historyState() {
        val history = listOf(
            SearchHistoryItem(id = 1L, query = "zelda", searchedAt = 0L),
            SearchHistoryItem(id = 2L, query = "cyberpunk", searchedAt = 0L)
        )
        every { viewModel.uiState } returns MutableStateFlow(
            SearchUiState(query = "", history = history, showHistory = true)
        )
        every { viewModel.results } returns flowOf(PagingData.empty())
        every { viewModel.effects } returns emptyFlow()

        composeRule.setContent {
            RawgTheme {
                SearchScreen(onGameClick = {}, viewModel = viewModel)
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/search_screen_history.png")
    }

    @Test
    fun resultsState() {
        val game = GamePreview(
            id = 1L,
            name = "Cyberpunk 2077",
            released = "2020-12-10",
            backgroundImage = null,
            rating = 4.3,
            platforms = emptyList(),
            platformsNames = "PC, PS5",
            genres = "RPG, Action",
            trailerUrl = null,
            trailerPreview = null
        )
        every { viewModel.uiState } returns MutableStateFlow(
            SearchUiState(query = "cyber", showHistory = false)
        )
        every { viewModel.results } returns flowOf(PagingData.from(listOf(game)))
        every { viewModel.effects } returns emptyFlow()

        composeRule.setContent {
            RawgTheme {
                SearchScreen(onGameClick = {}, viewModel = viewModel)
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/search_screen_results.png")
    }
}