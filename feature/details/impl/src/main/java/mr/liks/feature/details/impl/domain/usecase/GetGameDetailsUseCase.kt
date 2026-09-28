package mr.liks.feature.details.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GameDetails
import mr.liks.feature.details.impl.domain.repository.DetailsRepository

/**
 * Возвращает поток деталей игры из БД
 *
 * @property repository репозиторий деталей игры
 */
class GetGameDetailsUseCase(
    private val repository: DetailsRepository
) {
    operator fun invoke(gameId: Long): Flow<GameDetails?> =
        repository.observeDetails(gameId)
}