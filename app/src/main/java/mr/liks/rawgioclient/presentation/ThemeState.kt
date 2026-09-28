package mr.liks.rawgioclient.presentation

import mr.liks.core.model.ThemeMode

/**
 * Стейт темы приложения
 *
 * @property themeMode текущий режим из DataStore
 * @property dynamicColor включён ли Material You
 * @property isLoaded false, пока DataStore не отдал первое значение
 */
data class ThemeState(
    val themeMode: ThemeMode = ThemeMode.System,
    val dynamicColor: Boolean = false,
    val isLoaded: Boolean = false
)