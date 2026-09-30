package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto платформы
 *
 * @property id id
 * @property name название
 * @property slug слаг
 * @property image url логотипа
 * @property yearEnd
 * @property yearStart
 * @property gamesCount
 */
@Serializable
data class PlatformDto(
    val id: Long,
    val name: String,
    val slug: String,
    val image: String? = null,
    val yearEnd: String? = null,
    val yearStart: String? = null,
    val gamesCount: Int? = null,
)
