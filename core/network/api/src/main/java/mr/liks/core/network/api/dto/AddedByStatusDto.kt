package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Dto кол-ва добавления игры пользователями по типу
 *
 * @property yet в планах
 * @property owned в коллекции
 * @property beaten прошли
 * @property toplay в списке желаемого
 * @property dropped заброшено
 * @property playing играют сейчас
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