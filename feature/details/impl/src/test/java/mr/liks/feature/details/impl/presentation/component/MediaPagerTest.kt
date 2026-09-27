package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GameMedia
import mr.liks.core.model.Screenshot
import mr.liks.core.model.Trailer
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaPagerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mediaPager_showsItemsAndInvokesClick() {
        val media = GameMedia(
            trailers = listOf(
                Trailer(
                    id = 1L,
                    name = "Trailer",
                    preview = "preview",
                    data480 = "480",
                    dataMax = "max"
                )
            ),
            screenshots = listOf(
                Screenshot(
                    id = 2L,
                    image = "image",
                    width = 1920,
                    height = 1080
                )
            )
        )

        var clickedIndex = -1

        composeTestRule.setContent {
            RawgTheme {
                MediaPager(
                    media = media,
                    onItemClick = { clickedIndex = it }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Воспроизвести").assertIsDisplayed()

        composeTestRule.onAllNodes(hasClickAction())[0].performClick()

        assertEquals(0, clickedIndex)
    }
}