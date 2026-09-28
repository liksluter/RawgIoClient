package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsToggleRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `displays title and subtitle`() {
        composeRule.setContent {
            SettingsToggleRow(
                title = "Test Title",
                subtitle = "Test Subtitle",
                checked = false,
                onCheckedChange = {}
            )
        }

        composeRule.onNodeWithText("Test Title").assertExists()
        composeRule.onNodeWithText("Test Subtitle").assertExists()
    }

    @Test
    fun `clicking switch triggers callback`() {
        var newValue: Boolean? = null
        composeRule.setContent {
            SettingsToggleRow(
                title = "Toggle",
                checked = false,
                onCheckedChange = { newValue = it }
            )
        }

        composeRule.onNode(isToggleable()).performClick()
        assert(newValue == true)
    }

    @Test
    fun `disabled row does not trigger callback`() {
        composeRule.setContent {
            SettingsToggleRow(
                title = "Toggle",
                checked = false,
                onCheckedChange = {},
                enabled = false
            )
        }

        composeRule.onNode(isToggleable()).assertIsNotEnabled()
    }
}