package mr.liks.feature.settings.impl.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import org.junit.Test

class SetDynamicColorUseCaseTest {
    private val repository: SettingsRepository = mockk(relaxed = true)
    private val useCase = SetDynamicColorUseCase(repository)

    @Test
    fun `invoke calls repository setDynamicColor with true`() = runTest {
        useCase(true)
        coVerify(exactly = 1) { repository.setDynamicColor(true) }
    }

    @Test
    fun `invoke calls repository setDynamicColor with false`() = runTest {
        useCase(false)
        coVerify(exactly = 1) { repository.setDynamicColor(false) }
    }
}