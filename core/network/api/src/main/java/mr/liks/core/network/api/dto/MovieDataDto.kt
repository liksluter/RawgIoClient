package mr.liks.core.network.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Dto ведеоролика
 *
 * @property data480
 * @property max
 */
@Serializable
data class MovieDataDto(
    @SerialName("480") val data480: String? = null,
    val max: String? = null
)
