package mr.liks.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/** Ключи DataStore Preferences */
internal object SettingsKeys {
    val ThemeMode = stringPreferencesKey("theme_mode")
    val DynamicColor = booleanPreferencesKey("dynamic_color")
}