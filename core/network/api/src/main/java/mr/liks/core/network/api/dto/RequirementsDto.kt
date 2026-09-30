package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto системные требования для игры
 *
 * @property minimum минимальные
 * @property recommended рекомендуемые
 */
@Serializable
data class RequirementsDto(
    val minimum: String?,
    val recommended: String?
)