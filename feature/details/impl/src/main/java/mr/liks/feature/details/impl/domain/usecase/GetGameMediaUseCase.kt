package mr.liks.feature.details.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GameMedia
import mr.liks.feature.details.impl.domain.repository.DetailsRepository

/**
 * Возвращает поток медиа игры из БД
 *
 * @property repository репозиторий детайле игры
 */
class GetGameMediaUseCase(
    private val repository: DetailsRepository
) {
    operator fun invoke(gameId: Long): Flow<GameMedia> =
        repository.observeMedia(gameId)
}