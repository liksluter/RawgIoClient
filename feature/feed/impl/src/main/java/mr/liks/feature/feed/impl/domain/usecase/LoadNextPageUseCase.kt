package mr.liks.feature.feed.impl.domain.usecase

import mr.liks.feature.feed.impl.domain.repository.FeedRepository

/**
 * Догрузка следующей страницы
 *
 * @property repository репозиторий ленты
 */
class LoadNextPageUseCase(
    private val repository: FeedRepository
) {
    suspend operator fun invoke() = repository.loadNextPage()
}