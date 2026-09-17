package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность трейлера
 *
 * @property id id
 * @property gameId id игры
 * @property name название
 * @property preview url превью
 * @property data480 данные видео
 * @property dataMax максимум данных
 */
@Entity(
    tableName = "trailers",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("gameId")]
)
data class TrailerEntity(
    @PrimaryKey val id: Long,
    val gameId: Long,
    val name: String,
    val preview: String?,
    val data480: String?,
    val dataMax: String?
)
