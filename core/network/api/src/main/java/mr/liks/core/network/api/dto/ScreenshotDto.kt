package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto скриншота
 *
 * @property id id
 * @property image uri изображения
 * @property width ширина
 * @property height высота
 * @property isDeleted флаг удаления, 'true' если был удален
 */
@Serializable
data class ScreenshotDto(
    val id: Long,
    val image: String,
    val width: Int,
    val height: Int,
    val isDeleted: Boolean = false
)
