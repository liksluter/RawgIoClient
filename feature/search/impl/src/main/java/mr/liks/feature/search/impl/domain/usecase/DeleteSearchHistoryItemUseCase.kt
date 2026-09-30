package mr.liks.feature.search.impl.domain.usecase

import mr.liks.feature.search.impl.domain.repository.SearchRepository

/**
 * Удаляет запрос из истории
 *
 * @property repository репозиторий поиска
 */
class DeleteSearchHistoryItemUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) = repository.deleteQuery(query)
}