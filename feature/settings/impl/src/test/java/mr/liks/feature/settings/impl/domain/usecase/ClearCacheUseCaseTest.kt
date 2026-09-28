package mr.liks.feature.settings.impl.domain.usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import org.junit.Test

class ClearCacheUseCaseTest {
    private val repository: SettingsRepository = mockk(relaxed = true)
    private val useCase = ClearCacheUseCase(repository)

    @Test
    fun `invoke calls repository clearCache`() = runTest {
        useCase()
        coVerify(exactly = 1) { repository.clearCache() }
    }
}