package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto рейтинга
 *
 * @property id id
 * @property title заголовок
 * @property count кол-во проголосовавших
 * @property percent процент от общего рейтинга
 */
@Serializable
data class RatingDto(
    val id: Long,
    val title: String,
    val count: Int,
    val percent: Double
)