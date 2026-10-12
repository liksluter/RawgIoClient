package mr.liks.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode

/**
 * Хранилище настроек приложения
 *
 * @property dataStore реализация хранилища настроек [DataStore] из androidx
 */
class SettingsDataStore(
    private val dataStore: DataStore<Preferences>
) {
    /** [Flow] с настройками */
    val flow: Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            AppSettings(
                themeMode = prefs[SettingsKeys.ThemeMode]
                    ?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() }
                    ?: ThemeMode.System,
                dynamicColor = prefs[SettingsKeys.DynamicColor] ?: false,
                cacheSizeBytes = 0L
            )
        }

    /** Устанавливает тему в [mode] */
    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.ThemeMode] = mode.name
        }
    }

    /** Включает/выключает [enabled] динамические цвета */
    suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[SettingsKeys.DynamicColor] = enabled
        }
    }
}