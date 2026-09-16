package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Ответ со списком трейлеров
 *
 * @property count кол-во
 * @property results список трейлеров
 */
@Serializable
data class MoviesResponse(
    val count: Int,
    val results: List<MovieDto>
)
