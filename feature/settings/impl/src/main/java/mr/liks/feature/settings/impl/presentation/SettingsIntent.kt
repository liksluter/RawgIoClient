package mr.liks.feature.settings.impl.presentation

import mr.liks.core.model.ThemeMode

/** Намерения на экране настроек */
sealed interface SettingsIntent {

    /** Смена режима темы */
    data class SetTheme(val mode: ThemeMode) : SettingsIntent

    /** Переключение динамических цветов */
    data class SetDynamicColor(val enabled: Boolean) : SettingsIntent

    /** Очистка кеша */
    data object ClearCache : SettingsIntent

    /** Закрыть сообщение об ошибке */
    data object DismissError : SettingsIntent

    /** Открыть ссылку [url] */
    data class OpenUrl(val url: String) : SettingsIntent
}