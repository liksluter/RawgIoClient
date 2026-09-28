package mr.liks.feature.settings.impl.presentation

/** Эффекты экрана настроек */
sealed interface SettingsEffect {

    /** Показать снекбар */
    data class ShowSnackbar(val message: String) : SettingsEffect

    /** Открыть внешнюю ссылку в браузере */
    data class OpenUrl(val url: String) : SettingsEffect
}