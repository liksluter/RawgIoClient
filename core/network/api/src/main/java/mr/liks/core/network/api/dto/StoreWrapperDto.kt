package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Врапер dto магазина
 *
 * @property store dto магазина
 */
@Serializable
data class StoreWrapperDto(
    val store: StoreDto
)