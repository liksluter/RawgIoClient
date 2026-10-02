package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto магазина
 *
 * @property id id магазина
 * @property name название
 * @property slug слаг
 * @property domain домен
 * @property gamesCount кол-во игр
 */
@Serializable
data class StoreDto(
    val id: Long,
    val name: String,
    val slug: String,
    val domain: String?,
    val gamesCount: Int?
)