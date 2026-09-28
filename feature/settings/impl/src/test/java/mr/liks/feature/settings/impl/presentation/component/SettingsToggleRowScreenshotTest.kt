package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import mr.liks.core.designsystem.theme.RawgTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w360dp-h640dp-xhdpi")
class SettingsToggleRowScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun settingsToggleRow() {
        composeRule.setContent {
            RawgTheme {
                SettingsToggleRow(
                    title = "Динамические цвета",
                    subtitle = "Использовать цвета обоев (Android 12+)",
                    checked = true,
                    onCheckedChange = {}
                )
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/SettingsToggleRow.png")
    }
}