package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.Developer
import mr.liks.core.model.GameDetails
import mr.liks.core.model.Publisher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class DetailsHeaderScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailsHeader() {
        composeTestRule.setContent {
            RawgTheme {
                DetailsHeader(details = sampleDetails())
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage(
            filePath = "build/screenshots/details_header.png"
        )
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