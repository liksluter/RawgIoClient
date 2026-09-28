package mr.liks.feature.settings.impl.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCacheSizeUseCaseTest {
    private val repository: SettingsRepository = mockk()
    private val useCase = GetCacheSizeUseCase(repository)

    @Test
    fun `invoke returns cache size from repository`() = runTest {
        val expected = 1024L
        coEvery { repository.getCacheSizeBytes() } returns expected

        assertEquals(expected, useCase())
    }
}