package mr.liks.feature.feed.impl.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GamePreview
import mr.liks.feature.feed.impl.domain.repository.FeedRepository

/**
 * Возвращает поток страниц ленты
 *
 * @property repository репозиторий ленты
 */
class GetFeedPagingDataUseCase(
    private val repository: FeedRepository
) {
    operator fun invoke(): Flow<PagingData<GamePreview>> = repository.getPagingData()
}