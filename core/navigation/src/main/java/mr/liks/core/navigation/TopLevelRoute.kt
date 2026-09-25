package mr.liks.core.navigation

import kotlinx.serialization.Serializable

sealed interface TopLevelRoute: AppRoute {
    /** Лента */
    @Serializable
    data object Feed : TopLevelRoute
    /** Поиск */
    @Serializable
    data object Search : TopLevelRoute
    /** Настройки */
    @Serializable
    data object Settings : TopLevelRoute
}