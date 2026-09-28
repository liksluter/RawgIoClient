package mr.liks.feature.settings.impl.domain.usecase

import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Устанавливает режим темы
 *
 * @property repository репозиторий настроек
 */
class SetThemeUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(mode: ThemeMode) = repository.setTheme(mode)
}