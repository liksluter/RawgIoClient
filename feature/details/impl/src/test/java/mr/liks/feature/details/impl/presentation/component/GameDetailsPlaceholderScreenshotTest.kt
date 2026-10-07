package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import mr.liks.core.designsystem.theme.RawgTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class GameDetailsPlaceholderScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gameDetailsPlaceholder() {
        composeTestRule.setContent {
            RawgTheme {
                GameDetailsPlaceholder(contentPadding = PaddingValues())
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage(
            filePath = "build/screenshots/game_details_placeholder.png"
        )
    }
}