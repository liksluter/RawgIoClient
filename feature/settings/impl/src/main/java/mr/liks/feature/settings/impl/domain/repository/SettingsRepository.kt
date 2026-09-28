package mr.liks.feature.settings.impl.domain.repository

import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode

/** Контракт настроек */
interface SettingsRepository {

    /** Поток текущих настроек */
    fun observeSettings(): Flow<AppSettings>

    /** Устанавливает тему в [mode] */
    suspend fun setTheme(mode: ThemeMode)

    /** Включает/выключает [enabled] динамические цвета  */
    suspend fun setDynamicColor(enabled: Boolean)

    /** Размер кеша в байтах */
    suspend fun getCacheSizeBytes(): Long

    /** Полностью очищает кеш видео и изображений */
    suspend fun clearCache()
}