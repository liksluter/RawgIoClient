package mr.liks.feature.detais.api

import kotlinx.serialization.Serializable
import mr.liks.core.navigation.AppRoute

/**
 * Маршрут экрана деталей игры
 *
 * @property gameId id игры
 */
@Serializable
data class GameDetailsRoute(val gameId: Long) : AppRoute
