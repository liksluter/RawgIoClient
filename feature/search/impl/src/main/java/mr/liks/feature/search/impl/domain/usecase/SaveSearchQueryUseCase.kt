package mr.liks.feature.search.impl.domain.usecase

import mr.liks.core.common.ext.normalizeQuery
import mr.liks.feature.search.impl.domain.repository.SearchRepository

/**
 * Сохраняет запрос в историю
 *
 * @property repository репозиторий поиска
 */
class SaveSearchQueryUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(query: String) {
        val normalized = query.normalizeQuery()
        if (normalized.isBlank()) return
        repository.saveQuery(normalized)
    }
}