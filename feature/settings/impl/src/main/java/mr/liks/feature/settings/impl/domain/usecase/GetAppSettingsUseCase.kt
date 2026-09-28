package mr.liks.feature.settings.impl.domain.usecase

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.AppSettings
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Поток настроек приложения
 *
 * @property repository репозиторий настроек
 */
class GetAppSettingsUseCase(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.observeSettings()
}