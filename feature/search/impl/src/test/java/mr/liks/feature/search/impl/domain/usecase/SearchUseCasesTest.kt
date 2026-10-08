package mr.liks.feature.search.impl.domain.usecase

import androidx.paging.PagingData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.ext.normalizeQuery
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem
import mr.liks.feature.search.impl.domain.repository.SearchRepository
import org.junit.Assert.assertSame
import org.junit.Test

class SearchUseCasesTest {
    private val repository = mockk<SearchRepository>(relaxed = true)

    @Test
    fun `SearchGamesUseCase delegates to repository`() = runTest {
        val flow = flowOf(PagingData.empty<GamePreview>())
        every { repository.search("query") } returns flow

        val useCase = SearchGamesUseCase(repository)
        val result = useCase("query")

        assertSame(flow, result)
    }

    @Test
    fun `GetSearchHistoryUseCase delegates to repository`() = runTest {
        val flow = flowOf(emptyList<SearchHistoryItem>())
        every { repository.observeHistory() } returns flow

        val useCase = GetSearchHistoryUseCase(repository)
        val result = useCase()

        assertSame(flow, result)
    }

    @Test
    fun `SaveSearchQueryUseCase ignores blank`() = runTest {
        val useCase = SaveSearchQueryUseCase(repository)
        useCase("   ")

        coVerify(exactly = 0) { repository.saveQuery(any()) }
    }

    @Test
    fun `SaveSearchQueryUseCase saves normalized query`() = runTest {
        val raw = "  Game  "
        val normalized = raw.normalizeQuery()

        val useCase = SaveSearchQueryUseCase(repository)
        useCase(raw)

        coVerify { repository.saveQuery(normalized) }
    }

    @Test
    fun `DeleteSearchHistoryItemUseCase delegates`() = runTest {
        val useCase = DeleteSearchHistoryItemUseCase(repository)
        useCase("query")

        coVerify { repository.deleteQuery("query") }
    }
}