package mr.liks.feature.search.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.SearchHistoryItem
import mr.liks.feature.search.impl.domain.repository.SearchRepository

/**
 * Возвращает флоу истории поиска
 *
 * @property repository репозиторий поиска
 */
class GetSearchHistoryUseCase(
    private val repository: SearchRepository
) {
    operator fun invoke(): Flow<List<SearchHistoryItem>> = repository.observeHistory()
}