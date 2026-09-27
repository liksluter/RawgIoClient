package mr.liks.feature.details.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GameMedia
import mr.liks.core.model.Screenshot
import mr.liks.core.model.Trailer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class MediaPagerScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mediaPager() {
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

        composeTestRule.setContent {
            RawgTheme {
                MediaPager(
                    media = media,
                    onItemClick = {}
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage(
            filePath = "build/screenshots/media_pager.png"
        )
    }
}