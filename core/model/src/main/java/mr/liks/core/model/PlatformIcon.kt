package mr.liks.core.model

/**
 * Иконка платформы для отображения в карточке игры
 *
 * @property id платформы
 * @property name название
 * @property iconUrl URL иконки
 */
data class PlatformIcon(
    val id: Long,
    val name: String,
    val iconUrl: String?
)