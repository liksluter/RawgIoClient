package mr.liks.feature.details.impl.domain.usecase

import mr.liks.feature.details.impl.domain.repository.DetailsRepository

/**
 * Обновляет детали игры из сети
 *
 * @property repository репозиторий детайей игры
 */
class RefreshGameDetailsUseCase(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(gameId: Long) = repository.refreshDetails(gameId)
}