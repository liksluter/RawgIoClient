package mr.liks.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

/**
 * Хранилище настроек приложения
 *
 * @property context контекст
 */
class SettingsDataStore(
    private val context: Context
) {
    /** [Flow] с настройками */
    val flow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
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
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.ThemeMode] = mode.name
        }
    }

    /** Включает/выключает [enabled] динамисческие цвета */
    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.DynamicColor] = enabled
        }
    }
}