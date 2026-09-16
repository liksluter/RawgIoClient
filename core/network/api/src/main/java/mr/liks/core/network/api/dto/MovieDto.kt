package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto трейлера
 *
 * @property id id
 * @property name название
 * @property preview ссылка на превью
 * @property data данные видеоролика
 */
@Serializable
data class MovieDto(
    val id: Long,
    val name: String,
    val preview: String? = null,
    val data: MovieDataDto? = null
)
