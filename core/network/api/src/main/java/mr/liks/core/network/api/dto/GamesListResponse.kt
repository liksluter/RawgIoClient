package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Ответ со списком игр
 *
 * @property count кол-во результатов
 * @property results список игр
 */
@Serializable
data class GamesListResponse(
    val count: Int,
    val results: List<GameListDto>
)
