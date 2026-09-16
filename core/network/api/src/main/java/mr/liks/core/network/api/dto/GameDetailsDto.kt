package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto детальной информации об игре
 *
 * @property id id
 * @property slug
 * @property name название
 * @property description описание
 * @property descriptionRaw the description raw
 * @property released время выпуска игры
 * @property backgroundImage изображение обложки
 * @property rating рейтинг
 * @property metacritic рейтинг метакритика
 * @property playtime игровое время
 * @property website сайт
 * @property redditUrl ссылка на reddit
 * @property metacriticUrl ссылка на метакритик
 * @property developers разработчики
 * @property publishers издатели
 * @property genres жанры
 * @property platforms платформы
 */
@Serializable
data class GameDetailsDto(
    val id: Long,
    val slug: String,
    val name: String,
    val description: String? = null,
    val descriptionRaw: String? = null,
    val released: String? = null,
    val backgroundImage: String? = null,
    val rating: Double = 0.0,
    val metacritic: Int? = null,
    val playtime: Int? = null,
    val website: String? = null,
    val redditUrl: String? = null,
    val metacriticUrl: String? = null,
    val developers: List<DeveloperDto> = emptyList(),
    val publishers: List<PublisherDto> = emptyList(),
    val genres: List<GenreDto> = emptyList(),
    val platforms: List<PlatformWrapperDto> = emptyList()
)
