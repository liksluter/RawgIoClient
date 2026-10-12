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
 * @property labelId res id метки
 * @property icon иконка
 * @property order порядок
 */
enum class TopLevelRouteInfo(
    val route: AppRoute,
    val labelId: Int,
    val icon: ImageVector,
    val order: Int
) {
    FEED(
        route = TopLevelRoute.Feed,
        labelId = R.string.feed,
        icon = Icons.Filled.Home,
        order = 0
    ),
    SEARCH(
        route = TopLevelRoute.Search,
        labelId = R.string.search,
        icon = Icons.Filled.Search,
        order = 1
    ),
    SETTINGS(
        route = TopLevelRoute.Settings,
        labelId = R.string.settings,
        icon = Icons.Filled.Settings,
        order = 2
    );

    companion object {
        val ordered: List<TopLevelRouteInfo> = entries.sortedBy { it.order }

        fun fromRoute(route: AppRoute?): TopLevelRouteInfo? =
            entries.firstOrNull { it.route == route }
    }
}