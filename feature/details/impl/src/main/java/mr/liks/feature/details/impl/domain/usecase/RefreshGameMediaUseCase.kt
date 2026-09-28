package mr.liks.feature.details.impl.domain.usecase

import mr.liks.feature.details.impl.domain.repository.DetailsRepository

/**
 * Обновляет медиа игры из сети
 *
 * @property repository репозиторий деталей игры
 */
class RefreshGameMediaUseCase(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(gameId: Long) = repository.refreshMedia(gameId)
}