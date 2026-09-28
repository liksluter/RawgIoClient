package mr.liks.feature.settings.impl.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import org.junit.Test

class SetThemeUseCaseTest {
    private val repository: SettingsRepository = mockk(relaxed = true)
    private val useCase = SetThemeUseCase(repository)

    @Test
    fun `invoke calls repository setTheme with mode`() = runTest {
        useCase(ThemeMode.Dark)
        coVerify(exactly = 1) { repository.setTheme(ThemeMode.Dark) }
    }
}