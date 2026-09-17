package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность скриншота, таблица `screenshots`
 *
 * @property id id
 * @property gameId id игры
 * @property image url изображения
 * @property width ширина
 * @property height высота
 * @property isDeleted флаг удаления, `true` скриншот удален
 */
@Entity(
    tableName = "screenshots",
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
data class ScreenshotEntity(
    @PrimaryKey val id: Long,
    val gameId: Long,
    val image: String,
    val width: Int,
    val height: Int,
    val isDeleted: Boolean
)
