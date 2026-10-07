package mr.liks.feature.details.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.media.TrailerPlayerController
import mr.liks.core.model.GameDetails
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `initial loading shows placeholder and hides content`() {
        val viewModel = mockViewModel(GameDetailsUiState())

        setScreenContent(viewModel)

        composeTestRule.onNodeWithText("Об игре").assertDoesNotExist()
    }

    @Test
    fun `error state shows error message`() {
        val viewModel = mockViewModel(
            GameDetailsUiState(
                details = null,
                isLoadingDetails = false,
                errorMessage = "Не удалось загрузить игру"
            )
        )

        setScreenContent(viewModel)

        composeTestRule.onNodeWithText("Не удалось загрузить игру").assertIsDisplayed()
    }

    @Test
    fun `success state shows details content`() {
        val details = mockk<GameDetails>(relaxed = true) {
            every { name } returns "The Witcher 3"
            every { description } returns "Description"
            every { released } returns "2015-05-19"
            every { rating } returns 4.8
            every { genres } returns emptyList()
            every { platforms } returns emptyList()
            every { developers } returns emptyList()
            every { publishers } returns emptyList()
            every { backgroundImage } returns null
        }
        val viewModel = mockViewModel(
            GameDetailsUiState(
                details = details,
                isLoadingDetails = false,
                isLoadingMedia = false
            )
        )

        setScreenContent(viewModel)

        composeTestRule.onNodeWithText("The Witcher 3", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Об игре").assertIsDisplayed()
    }

    private fun setScreenContent(viewModel: GameDetailsViewModel) {
        composeTestRule.setContent {
            RawgTheme {
                GameDetailsScreen(
                    gameId = 1L,
                    contentPadding = PaddingValues(),
                    onBack = {},
                    viewModel = viewModel
                )
            }
        }
    }

    private fun mockViewModel(state: GameDetailsUiState): GameDetailsViewModel =
        mockk(relaxed = true) {
            every { uiState } returns MutableStateFlow(state)
            every { effects } returns emptyFlow()
            every { trailerPlayerController } returns mockk<TrailerPlayerController>(relaxed = true)
        }
}