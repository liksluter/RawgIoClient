package mr.liks.feature.feed.impl.domain.usecase

import mr.liks.feature.feed.impl.domain.repository.FeedRepository

/**
 * Форсированное обновление ленты
 *
 * @property repository репозиторий ленты
 */
class RefreshFeedUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke() = repository.refresh()
}