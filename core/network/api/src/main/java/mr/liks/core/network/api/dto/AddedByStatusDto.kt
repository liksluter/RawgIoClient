package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto кол-ва добавления игры пользователем по типу
 *
 * @property yet
 * @property owned
 * @property beaten
 * @property toplay
 * @property dropped
 * @property playing
 */
@Serializable
data class AddedByStatusDto(
    val yet: Int?,
    val owned: Int?,
    val beaten: Int?,
    val toplay: Int?,
    val dropped: Int?,
    val playing: Int?,
)