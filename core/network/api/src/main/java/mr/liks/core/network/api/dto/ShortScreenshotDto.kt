package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto скриншота для ленты
 *
 * @property id id
 * @property image ссылка на изображение
 */
@Serializable
data class ShortScreenshotDto(
    val id: Long,
    val image: String
)