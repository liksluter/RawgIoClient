package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CacheSizeRowTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun `displays formatted size and clear button`() {
        composeRule.setContent {
            CacheSizeRow(
                sizeBytes = 1024L,
                isClearing = false,
                onClear = {}
            )
        }

        composeRule.onNodeWithText("Размер кеша").assertExists()
        composeRule.onNodeWithText("1.0 КБ").assertExists()
        composeRule.onNodeWithText("Очистить").assertExists()
    }

    @Test
    fun `clear button disabled when size is zero`() {
        composeRule.setContent {
            CacheSizeRow(
                sizeBytes = 0L,
                isClearing = false,
                onClear = {}
            )
        }

        composeRule.onNodeWithText("Очистить").assertIsNotEnabled()
    }

    @Test
    fun `clear button triggers callback`() {
        var cleared = false
        composeRule.setContent {
            CacheSizeRow(
                sizeBytes = 1024L,
                isClearing = false,
                onClear = { cleared = true }
            )
        }

        composeRule.onNodeWithText("Очистить").performClick()
        assert(cleared)
    }

    @Test
    fun `shows progress indicator when clearing`() {
        composeRule.setContent {
            CacheSizeRow(
                sizeBytes = 1024L,
                isClearing = true,
                onClear = {}
            )
        }

        composeRule.onNodeWithText("Очистить").assertDoesNotExist()
    }

    @Test
    fun `formatBytes returns correct strings`() {
        assertEquals("0 Б", formatBytes(0))
        assertEquals("500 Б", formatBytes(500))
        assertEquals("1.0 КБ", formatBytes(1024))
        assertEquals("1.0 МБ", formatBytes(1024 * 1024))
        assertEquals("1.0 ГБ", formatBytes(1024L * 1024 * 1024))
    }
}