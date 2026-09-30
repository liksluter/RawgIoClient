package mr.liks.feature.search.impl.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GamePreview
import mr.liks.feature.search.impl.domain.repository.SearchRepository

/**
 * Ищет игры по запросу
 *
 * @property repository репозиторий поиска
 */
class SearchGamesUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String): Flow<PagingData<GamePreview>> =
        repository.search(query)
}