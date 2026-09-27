package mr.liks.feature.details.impl.domain.repository

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GameDetails
import mr.liks.core.model.GameMedia

/** Контракт экрана деталей */
interface DetailsRepository {
    /** @return [Flow] дейталей игры из БД */
    fun observeDetails(gameId: Long): Flow<GameDetails?>

    /** @return [Flow] медиа игры из БД */
    fun observeMedia(gameId: Long): Flow<GameMedia>

    /** Обновить детали игры с идентификатором [gameId] из сети */
    suspend fun refreshDetails(gameId: Long)

    /** Обновить медиа игры с идентификатором [gameId] из сети */
    suspend fun refreshMedia(gameId: Long)
}