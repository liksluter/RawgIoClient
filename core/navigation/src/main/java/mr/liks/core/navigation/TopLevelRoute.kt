package mr.liks.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Top-level маршруты, которые показываются в нижнем тулбаре
 *
 * @property route маршрут
 * @property label метка
 * @property icon иконка
 * @property order порядок
 */
enum class TopLevelRoute(
    val route: AppRoute,
    val label: String,
    val icon: ImageVector,
    val order: Int
) {
    FEED(
        route = AppRoute.Feed,
        label = "Лента",
        icon = Icons.Filled.Home,
        order = 0
    ),
    SEARCH(
        route = AppRoute.Search,
        label = "Поиск",
        icon = Icons.Filled.Search,
        order = 1
    ),
    SETTINGS(
        route = AppRoute.Settings,
        label = "Настройки",
        icon = Icons.Filled.Settings,
        order = 2
    );

    companion object {
        val ordered: List<TopLevelRoute> = entries.sortedBy { it.order }

        fun fromRoute(route: AppRoute?): TopLevelRoute? =
            entries.firstOrNull { it.route == route }
    }
}