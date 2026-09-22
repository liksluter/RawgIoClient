package mr.liks.feature.feed.impl.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import mr.liks.core.model.GamePreview
import mr.liks.core.model.PlatformIcon
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xhdpi")
class GameFeedCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val fakeGame = GamePreview(
        id = 1L,
        name = "Test Game",
        backgroundImage = "https://example.com/image.jpg",
        rating = 4.5,
        platforms = listOf(PlatformIcon(1L, "PC", "pc.png")),
        platformsNames = "PC",
        feedOrder = 0L,
        trailerUrl = null,
        trailerPreview = null
    )

    @Test
    fun `displays game name`() {
        composeRule.setContent {
            GameFeedCard(game = fakeGame, isVisible = true, onClick = {})
        }
        composeRule.onNodeWithText("Test Game", substring = true).assertIsDisplayed()
    }

    @Test
    fun `displays platform name`() {
        composeRule.setContent {
            GameFeedCard(game = fakeGame, isVisible = true, onClick = {})
        }
        composeRule.onNodeWithText("PC").assertIsDisplayed()
    }

    @Test
    fun `click triggers callback`() {
        var clicked = false
        composeRule.setContent {
            GameFeedCard(game = fakeGame, isVisible = true, onClick = { clicked = true })
        }
        composeRule.onNodeWithText("Test Game", substring = true).performClick()
        assert(clicked)
    }
}