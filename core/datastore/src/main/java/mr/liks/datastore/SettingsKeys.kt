package mr.liks.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/** Ключи DataStore Preferences */
internal object SettingsKeys {
    /** Тема */
    val ThemeMode = stringPreferencesKey("theme_mode")
    /** Флаг динамических цветов */
    val DynamicColor = booleanPreferencesKey("dynamic_color")
}