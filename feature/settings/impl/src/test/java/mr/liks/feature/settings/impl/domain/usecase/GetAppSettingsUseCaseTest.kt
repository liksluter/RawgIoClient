package mr.liks.feature.settings.impl.domain.usecase

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAppSettingsUseCaseTest {
    private val repository: SettingsRepository = mockk()
    private val useCase = GetAppSettingsUseCase(repository)

    @Test
    fun `invoke returns flow from repository`() = runTest {
        val settings = AppSettings(themeMode = ThemeMode.Dark)
        every { repository.observeSettings() } returns flowOf(settings)

        useCase().test {
            assertEquals(settings, awaitItem())
            awaitComplete()
        }
    }
}