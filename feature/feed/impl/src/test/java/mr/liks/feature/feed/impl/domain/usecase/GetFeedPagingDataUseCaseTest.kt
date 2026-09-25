package mr.liks.feature.feed.impl.domain.usecase

import androidx.paging.PagingData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mr.liks.core.model.GamePreview
import mr.liks.feature.feed.impl.domain.repository.FeedRepository
import org.junit.Test

class GetFeedPagingDataUseCaseTest {
    private val repository: FeedRepository = mockk(relaxed = true)

    @Test
    fun `GetFeedPagingDataUseCase returns repository flow`() = runTest {
        val fakeFlow = flowOf(PagingData.empty<GamePreview>())
        every { repository.getPagingData() } returns fakeFlow

        val useCase = GetFeedPagingDataUseCase(repository)
        val result = useCase()

        assert(result === fakeFlow)
    }

    @Test
    fun `RefreshFeedUseCase calls repository refresh`() = runTest {
        val useCase = RefreshFeedUseCase(repository)
        useCase()
        coVerify(exactly = 1) { repository.refresh() }
    }

    @Test
    fun `LoadNextPageUseCase calls repository loadNextPage`() = runTest {
        val useCase = LoadNextPageUseCase(repository)
        useCase()
        coVerify(exactly = 1) { repository.loadNextPage() }
    }
}