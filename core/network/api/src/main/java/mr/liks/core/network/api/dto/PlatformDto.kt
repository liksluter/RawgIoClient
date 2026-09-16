package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto платформы
 *
 * @property id id
 * @property name название
 * @property slug
 * @property imageBackground логотип
 */
@Serializable
data class PlatformDto(
    val id: Long,
    val name: String,
    val slug: String,
    val imageBackground: String? = null
)
