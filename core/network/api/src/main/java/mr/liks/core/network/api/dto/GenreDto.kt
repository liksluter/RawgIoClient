package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto жанра
 *
 * @property id id
 * @property name название
 * @property slug
 */
@Serializable
data class GenreDto(
    val id: Long,
    val name: String,
    val slug: String
)
