package mr.liks.feature.settings.impl.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.usecase.GetAppSettingsUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "w360dp-h640dp-xhdpi")
class SettingsScreenScreenshotTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun settingsScreen() {
        val getAppSettings: GetAppSettingsUseCase = mockk()
        every { getAppSettings() } returns flowOf(
            AppSettings(
                themeMode = ThemeMode.System,
                dynamicColor = true,
                cacheSizeBytes = 1024 * 1024 * 5L
            )
        )
        val viewModel = SettingsViewModel(
            getAppSettings = getAppSettings,
            setTheme = mockk(relaxed = true),
            setDynamicColor = mockk(relaxed = true),
            getCacheSize = mockk(relaxed = true),
            clearCache = mockk(relaxed = true),
            appVersion = "1.0.0",
            logger = mockk(relaxed = true)
        )

        composeRule.setContent {
            RawgTheme {
                SettingsScreen(viewModel = viewModel)
            }
        }

        composeRule.onRoot().captureRoboImage("build/screenshots/SettingsScreen.png")
    }
}