package mr.liks.feature.settings.impl.domain.usecase

import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Включает/выключает динамические цвета Material You (Android 12+)
 *
 * @property repository репозиторий настроек
 */
class SetDynamicColorUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDynamicColor(enabled)
}