package mr.liks.core.model

/**
 * Подробная модель игры
 *
 * @property id игры
 * @property name название игры
 * @property backgroundImage фоновая картинка для заголовка
 * @property description описание
 * @property descriptionRaw описание в plain text
 * @property released дата релиза
 * @property rating рейтинг
 * @property metacritic оценка metacritic
 * @property playtime среднее время прохождения в часах
 * @property website официальный сайт
 * @property redditUrl ссылка на reddit
 * @property metacriticUrl ссылка на страницу metacritic
 * @property developers список разработчиков
 * @property publishers список издателей
 * @property genres список жанров
 * @property platforms список платформ с иконками
 */
data class GameDetails(
    val id: Long,
    val name: String,
    val backgroundImage: String?,
    val description: String?,
    val descriptionRaw: String?,
    val released: String?,
    val rating: Double,
    val metacritic: Int?,
    val playtime: Int?,
    val website: String?,
    val redditUrl: String?,
    val metacriticUrl: String?,
    val developers: List<Developer>,
    val publishers: List<Publisher>,
    val genres: List<Genre>,
    val platforms: List<PlatformIcon>
)

/**
 * Разработчик
 *
 * @property id id
 * @property name название
 */
data class Developer(
    val id: Long,
    val name: String
)

/**
 * Издатель
 *
 * @property id id
 * @property name название
 */
data class Publisher(
    val id: Long,
    val name: String
)

/**
 * Жанр
 *
 * @property id id
 * @property name название
 */
data class Genre(
    val id: Long,
    val name: String
)
