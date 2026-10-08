package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AboutSectionTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `displays version and data provider`() {
        composeRule.setContent {
            AboutSection(
                appVersion = "1.2.3",
                onOpenUrl = {}
            )
        }

        composeRule.onNodeWithText("Версия").assertExists()
        composeRule.onNodeWithText("1.2.3").assertExists()
        composeRule.onNodeWithText("Данные предоставлены").assertExists()
        composeRule.onNodeWithText("rawg.io").assertExists()
    }

    @Test
    fun `click on rawg io opens url`() {
        var openedUrl: String? = null
        composeRule.setContent {
            AboutSection(
                appVersion = "1.0.0",
                onOpenUrl = { openedUrl = it }
            )
        }

        composeRule.onNodeWithText("rawg.io").performClick()
        assert(openedUrl == "https://rawg.io")
    }
}