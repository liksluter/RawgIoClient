package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Врапер для изначальной платформы
 *
 * @property platform информация о платформе [ParentPlatformDto]
 */
@Serializable
data class ParentPlatformWrapperDto(
    val platform: ParentPlatformDto,
)
