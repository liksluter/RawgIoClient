package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto изначальной платформа на которой вышла игра
 *
 * @property id id платформы
 * @property name название
 * @property slug слаг
 */
@Serializable
data class ParentPlatformDto(
    val id: Long,
    val name: String,
    val slug: String,
)
