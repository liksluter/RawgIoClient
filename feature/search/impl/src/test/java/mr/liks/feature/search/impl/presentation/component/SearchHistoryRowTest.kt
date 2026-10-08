package mr.liks.feature.search.impl.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.feature.search.impl.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchHistoryRowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displaysQuery() {
        composeRule.setContent {
            RawgTheme {
                SearchHistoryRow(query = "zelda", onClick = {}, onDelete = {})
            }
        }

        composeRule.onNodeWithText("zelda").assertExists()
    }

    @Test
    fun clickRowCallsOnClick() {
        var clicked = false
        composeRule.setContent {
            RawgTheme {
                SearchHistoryRow(
                    query = "zelda",
                    onClick = { clicked = true },
                    onDelete = {}
                )
            }
        }

        composeRule.onNodeWithText("zelda").performClick()

        assertTrue(clicked)
    }

    @Test
    fun clickDeleteCallsOnDelete() {
        var deleted = false
        composeRule.setContent {
            RawgTheme {
                SearchHistoryRow(
                    query = "zelda",
                    onClick = {},
                    onDelete = { deleted = true }
                )
            }
        }

        val desc = composeRule.activity.getString(R.string.delete_search_content_description)
        composeRule.onNodeWithContentDescription(desc).performClick()

        assertTrue(deleted)
    }
}