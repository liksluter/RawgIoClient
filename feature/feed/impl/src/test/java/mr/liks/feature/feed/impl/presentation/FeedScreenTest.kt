package mr.liks.feature.feed.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.test.junit4.createComposeRule
import dev.chrisbanes.haze.HazeState
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w360dp-h640dp-xhdpi")
class FeedScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setup() {
        startKoin {
            modules(
                module {
                    viewModel { FeedViewModel(
                        getFeedPagingData = mockk(relaxed = true),
                        refreshFeed = mockk(relaxed = true),
                        logger = mockk(relaxed = true)
                    ) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `screen renders without crash`() {
        composeRule.setContent {
            FeedScreen(
                onGameClick = {},
                hazeState = HazeState(),
                listState = LazyListState(),
                contentPadding = PaddingValues()
            )
        }
        composeRule.waitForIdle()
    }
}