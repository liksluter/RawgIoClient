package mr.liks.feature.search.impl.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GamePreview
import mr.liks.feature.search.impl.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchResultItemTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val game = GamePreview(
        id = 1L,
        name = "Cyberpunk 2077",
        released = "2020-12-10",
        backgroundImage = "url",
        rating = 4.3,
        platforms = emptyList(),
        platformsNames = "PC, PS5",
        genres = "RPG, Action",
        trailerUrl = null,
        trailerPreview = null
    )

    @Test
    fun displaysGameInfo() {
        composeRule.setContent {
            RawgTheme {
                SearchResultItem(game = game, onClick = {})
            }
        }

        composeRule.onNodeWithText("Cyberpunk 2077").assertExists()
        composeRule.onNodeWithText("Релиз: 2020-12-10").assertExists()
        composeRule.onNodeWithText("Жанр: RPG, Action").assertExists()

        val platforms = composeRule.activity.getString(
            R.string.platform_template,
            "PC, PS5"
        )
        composeRule.onNodeWithText(platforms).assertExists()
    }

    @Test
    fun clickCallsOnClick() {
        var clicked = false
        composeRule.setContent {
            RawgTheme {
                SearchResultItem(game = game, onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Cyberpunk 2077").performClick()

        assertTrue(clicked)
    }
}