package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto списка игр
 *
 * @property id id
 * @property slug
 * @property name название
 * @property released дата релиза
 * @property backgroundImage фоновое изображение
 * @property rating рейтинг
 * @property ratingsCount кол-во голосов в рейтинге
 * @property metacritic оценка метакритика
 * @property playtime игровое вермя в часах
 * @property platforms платформы на которые издана игра
 */
@Serializable
data class GameListDto(
    val id: Long,
    val slug: String,
    val name: String,
    val released: String? = null,
    val backgroundImage: String? = null,
    val rating: Double = 0.0,
    val ratingsCount: Int = 0,
    val metacritic: Int? = null,
    val playtime: Int? = null,
    val platforms: List<PlatformWrapperDto> = emptyList()
)
