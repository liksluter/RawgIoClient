package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto рейтинга esrb (возрастной рейтинг)
 *
 * @property id id
 * @property name название
 * @property slug слаг
 */
@Serializable
data class EsrbRatingDto(
    val id: Long,
    val name: String,
    val slug: String,
    val nameEn: String?,
    val nameRu: String?,
)