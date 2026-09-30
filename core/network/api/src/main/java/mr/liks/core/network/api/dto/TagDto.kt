package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dtp тега
 *
 * @property id id тега
 * @property name название
 * @property slug слаг
 * @property language язык
 * @property gamesCount кол-во игр
 */
@Serializable
data class TagDto(
    val id: Long,
    val name: String,
    val slug: String,
    val language: String,
    val gamesCount: Int?
)