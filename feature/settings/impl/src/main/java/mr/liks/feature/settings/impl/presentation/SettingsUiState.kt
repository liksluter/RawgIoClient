package mr.liks.feature.settings.impl.presentation

import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode

/**
 * Состояние экрана настроек
 *
 * @property settings текущие настройки
 * @property isClearingCache флаг процесса очистки кеша
 * @property supportsDynamicColor поддержка dynamic color
 * @property appVersion версия приложения для секции
 * @property errorMessage ошибка
 */
data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isClearingCache: Boolean = false,
    val supportsDynamicColor: Boolean = false,
    val appVersion: String = "",
    val errorMessage: String? = null
) {
    val themeMode: ThemeMode get() = settings.themeMode
    val dynamicColor: Boolean get() = settings.dynamicColor
    val cacheSizeBytes: Long get() = settings.cacheSizeBytes
}