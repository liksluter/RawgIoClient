package mr.liks.feature.search.impl.presentation.component

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
@Config(sdk = [34], qualifiers = "w360dp-h640dp-xhdpi")
class SearchComponentsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun searchHistoryRow() {
        composeRule.setContent {
            RawgTheme {
                SearchHistoryRow(query = "zelda", onClick = {}, onDelete = {})
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/search_history_row.png")
    }

    @Test
    fun searchResultItem() {
        composeRule.setContent {
            RawgTheme {
                SearchResultItem(
                    game = mr.liks.core.model.GamePreview(
                        id = 1L,
                        name = "Cyberpunk 2077",
                        released = "2020-12-10",
                        backgroundImage = null,
                        rating = 4.3,
                        platforms = emptyList(),
                        platformsNames = "PC, PS5",
                        genres = "RPG, Action",
                        trailerUrl = null,
                        trailerPreview = null
                    ),
                    onClick = {}
                )
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/search_result_item.png")
    }

    @Test
    fun searchResultPlaceholderList() {
        composeRule.setContent {
            RawgTheme {
                SearchResultPlaceholderList(itemCount = 5)
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/search_result_placeholder_list.png")
    }
}