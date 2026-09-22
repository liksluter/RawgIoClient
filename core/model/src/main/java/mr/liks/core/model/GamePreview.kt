package mr.liks.core.model

/**
 * Модель игры для ленты и результатов поиска
 *
 * @property id id игры
 * @property name название игры
 * @property backgroundImage URL фонового изображения
 * @property rating рейтинг
 * @property platforms иконки платформ, на которых вышла игра
 * @property feedOrder порядок элемента в ленте // todo для тестов, убрать после стабилизации
 * @property trailerUrl URL видео-трейлера
 * @property trailerPreview URL превью трейлера
 */
data class GamePreview(
    val id: Long,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val platforms: List<PlatformIcon>,
    val platformsNames: String?,
    val feedOrder: Long,
    val trailerUrl: String?,
    val trailerPreview: String?,
)