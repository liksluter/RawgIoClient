package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto разработчика игры
 *
 * @property id id
 * @property name название
 * @property slug
 */
@Serializable
data class DeveloperDto(
    val id: Long,
    val name: String,
    val slug: String
)
