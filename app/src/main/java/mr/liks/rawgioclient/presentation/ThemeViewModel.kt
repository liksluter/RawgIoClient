package mr.liks.rawgioclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mr.liks.feature.settings.impl.domain.usecase.GetAppSettingsUseCase

/**
 * [ViewModel] для наблюдения за сменой темы
 *
 * @param getAppSettings usecase с настройками приложения
 */
class ThemeViewModel(
    getAppSettings: GetAppSettingsUseCase
) : ViewModel() {
    val themeState: StateFlow<ThemeState> = getAppSettings()
        .map { settings ->
            ThemeState(
                themeMode = settings.themeMode,
                dynamicColor = settings.dynamicColor,
                isLoaded = true
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ThemeState()
        )
}