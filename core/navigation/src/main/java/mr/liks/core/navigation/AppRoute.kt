package mr.liks.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** Базовый маршрут приложения */
@Serializable
sealed interface AppRoute : NavKey {
    /** Лента */
    @Serializable
    data object Feed : AppRoute
    /** Поиск */
    @Serializable
    data object Search : AppRoute
    /** НАстройки */
    @Serializable
    data object Settings : AppRoute
}