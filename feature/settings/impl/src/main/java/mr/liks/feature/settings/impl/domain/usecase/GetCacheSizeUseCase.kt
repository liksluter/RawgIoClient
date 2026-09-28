package mr.liks.feature.settings.impl.domain.usecase

import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Текущий размер дискового кеша
 *
 * @property repository репозиторий настроек
 */
class GetCacheSizeUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): Long = repository.getCacheSizeBytes()
}