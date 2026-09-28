package mr.liks.feature.settings.impl.presentation

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.domain.usecase.ClearCacheUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetAppSettingsUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetCacheSizeUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetDynamicColorUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetThemeUseCase

/**
 * [ViewModel] экрана настроек
 *
 * @property getAppSettings usecase для получения настроек приложения
 * @property setTheme usecase для установки темы
 * @property setDynamicColor usecase для включения/выключения динамических уветов
 * @property getCacheSize usecase дял получения размера кеша
 * @property clearCache usecase для очистки кеша
 * @property appVersion версия приложения
 * @property logger логер
 */
class SettingsViewModel(
    private val getAppSettings: GetAppSettingsUseCase,
    private val setTheme: SetThemeUseCase,
    private val setDynamicColor: SetDynamicColorUseCase,
    private val getCacheSize: GetCacheSizeUseCase,
    private val clearCache: ClearCacheUseCase,
    private val appVersion: String = "0.0.1", // todo передавать версию из BuildConfig
    private val logger: AppLogger
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            supportsDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
            appVersion = appVersion
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effects = Channel<SettingsEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<SettingsEffect> = _effects.receiveAsFlow()

    init {
        observeSettings()
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SetTheme -> onSetTheme(intent.mode)
            is SettingsIntent.SetDynamicColor -> onSetDynamicColor(intent.enabled)
            SettingsIntent.ClearCache -> onClearCache()
            SettingsIntent.DismissError ->
                _uiState.update { it.copy(errorMessage = null) }
            is SettingsIntent.OpenUrl -> onOpenUrl(intent.url)
        }
    }

    private fun observeSettings() {
        getAppSettings()
            .onEach { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
            .launchIn(viewModelScope)
    }

    private fun onSetTheme(mode: ThemeMode) {
        viewModelScope.launch {
            runCatching { setTheme(mode) }
                .onFailure { t ->
                    logger.e(t, "Failed to set theme")
                    _uiState.update { it.copy(errorMessage = t.message ?: "Ошибка") }
                }
        }
    }

    private fun onSetDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            runCatching { setDynamicColor(enabled) }
                .onFailure { t ->
                    logger.e(t, "Failed to set dynamic color")
                    _uiState.update { it.copy(errorMessage = t.message ?: "Ошибка") }
                }
        }
    }

    private fun onClearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearingCache = true, errorMessage = null) }
            runCatching { clearCache() }
                .onSuccess {
                    val newSize = getCacheSize()
                    _uiState.update {
                        it.copy(
                            isClearingCache = false,
                            settings = it.settings.copy(cacheSizeBytes = newSize)
                        )
                    }
                    _effects.send(SettingsEffect.ShowSnackbar("Кеш очищен"))
                }
                .onFailure { t ->
                    logger.e(t, "Failed to clear cache")
                    _uiState.update {
                        it.copy(
                            isClearingCache = false,
                            errorMessage = t.message ?: "Не удалось очистить кеш"
                        )
                    }
                }
        }
    }

    private fun onOpenUrl(url: String) {
        viewModelScope.launch {
            _effects.send(SettingsEffect.OpenUrl(url))
        }
    }
}