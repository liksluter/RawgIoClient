package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto врапера информации о платформе
 *
 * @property platform dto платформы
 * @property releasedAt время выхода игры под текущую платформу
 */
@Serializable
data class PlatformWrapperDto(
    val platform: PlatformDto,
    val releasedAt: String? = null
)
