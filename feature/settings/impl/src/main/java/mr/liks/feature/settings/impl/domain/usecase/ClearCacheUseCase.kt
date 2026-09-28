package mr.liks.feature.settings.impl.domain.usecase

import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Очищает кеш видео и изображений
 *
 * @property repository репозиторий настроек
 */
class ClearCacheUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke() = repository.clearCache()
}