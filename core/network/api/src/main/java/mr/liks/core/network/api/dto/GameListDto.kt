package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto списка игр
 *
 * @property id id
 * @property slug слаг
 * @property name название
 * @property released дата релиза
 * @property tba флаг To Be Announced
 * @property backgroundImage фоновое изображение
 * @property rating рейтинг
 * @property ratingTop самая высокая оценка в рейтинге
 * @property ratings список с детальной информацией о ретингах
 * @property ratingsCount кол-во голосов в рейтинге
 * @property reviewsTextCount кол-во текстовых отзывов
 * @property added общее кол-во добавивших игру
 * @property addedByStatus детальная информация по добавившим игру
 * @property metacritic оценка метакритика
 * @property playtime игровое вермя в часах
 * @property suggestionsCount кол-во предложений
 * @property updated дата обновления информаии об игре
 * @property reviewsCount кол-во ревью
 * @property saturatedColor
 * @property dominantColor
 * @property platforms платформы на которые издана игра
 * @property parentPlatform изначальные платформы на которых вышла игра
 * @property genres жанры игры
 * @property stores магазины, в которых вышла игра
 * @property tags теги игры
 * @property esrbRating возрастной рейтинг
 * @property shortScreenshots скриншоты
 */
@Serializable
data class GameListDto(
    val id: Long,
    val slug: String,
    val name: String,
    val released: String? = null,
    val tba: Boolean = false,
    val backgroundImage: String? = null,
    val rating: Double = 0.0,
    val ratingTop: Int = 0,
    val ratings: List<RatingDto> = emptyList(),
    val ratingsCount: Int = 0,
    val reviewsTextCount: Int = 0,
    val added: Int = 0,
    val addedByStatus: AddedByStatusDto? = null,
    val metacritic: Int? = null,
    val playtime: Int? = null,
    val suggestionsCount: Int = 0,
    val updated: String? = null,
    val reviewsCount: Int = 0,
    val saturatedColor: String? = null,
    val dominantColor: String? = null,
    val platforms: List<PlatformWrapperDto> = emptyList(),
    val parentPlatform: List<ParentPlatformWrapperDto> = emptyList(),
    val genres: List<GenreDto> = emptyList(),
    val stores: List<StoreWrapperDto> = emptyList(),
    val tags: List<TagDto> = emptyList(),
    val esrbRating: EsrbRatingDto? = null,
    val shortScreenshots: List<ShortScreenshotDto> = emptyList()
)
