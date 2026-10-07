package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.mockk
import io.mockk.verify
import mr.liks.core.media.TrailerPlayerController
import mr.liks.core.model.GameMedia
import mr.liks.core.model.Screenshot
import mr.liks.core.model.Trailer
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaViewerDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var playerController: TrailerPlayerController

    @Before
    fun setUp() {
        playerController = mockk(relaxed = true)
    }

    @Test
    fun viewerDialog_closeButtonDismisses() {
        val media = mediaWithScreenshot()

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 0,
                playerController = playerController,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Закрыть").performClick()

        assertTrue(dismissed)
    }

    @Test
    fun viewerDialog_emptyMediaDismissesImmediately() {
        val media = GameMedia(trailers = emptyList(), screenshots = emptyList())

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 0,
                playerController = playerController,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.waitForIdle()

        assertTrue(dismissed)

        composeTestRule.onNodeWithContentDescription("Закрыть").assertDoesNotExist()
    }

    @Test
    fun viewerDialog_initialIndexOutOfBoundsIsCoerced() {
        val media = mediaWithScreenshot()

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 99,
                playerController = playerController,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Закрыть").performClick()
        assertTrue(dismissed)
    }

    @Test
    fun viewerDialog_negativeInitialIndexIsCoerced() {
        val media = mediaWithScreenshot()

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = -5,
                playerController = playerController,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.waitForIdle()

        assertFalse(dismissed)

        composeTestRule.onNodeWithContentDescription("Закрыть").performClick()
        assertTrue(dismissed)
    }

    @Test
    fun viewerDialog_trailerWithoutPlaybackUrlFallsBackToPreview() {
        val trailer = Trailer(
            id = 1L,
            name = "Trailer",
            preview = "preview",
            data480 = null,
            dataMax = null
        )
        val media = GameMedia(trailers = listOf(trailer), screenshots = emptyList())

        var dismissed = false

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 0,
                playerController = playerController,
                onDismiss = { dismissed = true }
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Закрыть").assertExists()
    }

    @Test
    fun viewerDialog_screenshotOnStartPausesPlayer() {
        val media = mediaWithScreenshot()

        composeTestRule.setContent {
            MediaViewerDialog(
                media = media,
                initialIndex = 0,
                playerController = playerController,
                onDismiss = {}
            )
        }

        composeTestRule.waitForIdle()

        verify { playerController.pause() }
    }

    private fun mediaWithScreenshot() = GameMedia(
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
}