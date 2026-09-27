package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.Developer
import mr.liks.core.model.GameDetails
import mr.liks.core.model.Publisher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsHeaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun header_displaysNameRatingAndDeveloper() {
        composeTestRule.setContent {
            RawgTheme {
                DetailsHeader(details = sampleDetails())
            }
        }

        composeTestRule.onNodeWithText("The Witcher 3").assertIsDisplayed()
        composeTestRule.onNodeWithText("Разработчик:").assertIsDisplayed()
        composeTestRule.onNodeWithText("CD Projekt Red").assertIsDisplayed()
        composeTestRule.onNodeWithText("Издатель:").assertIsDisplayed()
        composeTestRule.onNodeWithText("CD Projekt").assertIsDisplayed()
    }

    private fun sampleDetails(): GameDetails = mockk(relaxed = true) {
        every { id } returns 1L
        every { name } returns "The Witcher 3"
        every { backgroundImage } returns null
        every { description } returns "Description"
        every { descriptionRaw } returns "<p>Description</p>"
        every { released } returns "2015-05-19"
        every { rating } returns 4.8
        every { metacritic } returns 92
        every { playtime } returns 50
        every { website } returns null
        every { redditUrl } returns null
        every { metacriticUrl } returns null
        every { developers } returns listOf(Developer(1, "CD Projekt Red"))
        every { publishers } returns listOf(Publisher(1, "CD Projekt"))
        every { genres } returns emptyList()
        every { platforms } returns emptyList()
    }
}