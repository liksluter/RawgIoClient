package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Ответ со списком игр
 *
 * @property count кол-во результатов
 * @property next следующая страница
 * @property previous предыдущая страница
 * @property results список игр
 */
@Serializable
data class GamesListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GameListDto>
)
