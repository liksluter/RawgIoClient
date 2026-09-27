package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import mr.liks.core.model.GameMedia
import mr.liks.core.model.Screenshot
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaViewerDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun viewerDialog_closeButtonDismisses() {
        val media = GameMedia(
            trailers = emptyList(),
            screenshots = listOf(
                Screenshot(
                    id = 1L,
                    image = "image",
                    width = 1920,
                    height = 1080
                )
            )
        )

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 0,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Закрыть").performClick()

        assertTrue(dismissed)
    }
}