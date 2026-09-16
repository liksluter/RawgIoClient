package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto издателя
 *
 * @property id id
 * @property name название
 * @property slug
 */
@Serializable
data class PublisherDto(
    val id: Long,
    val name: String,
    val slug: String
)
