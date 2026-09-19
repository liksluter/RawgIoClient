package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность деталей игры, таблица `game_details`
 *
 * @property gameId id игры
 * @property description описание
 * @property descriptionRaw описание
 * @property website url сайта
 * @property redditUrl url на Reddit
 * @property metacriticUrl url на метакритике
 */
@Entity(
    tableName = "game_details",
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
data class GameDetailsEntity(
    @PrimaryKey val gameId: Long,
    val description: String?,
    val descriptionRaw: String?,
    val website: String?,
    val redditUrl: String?,
    val metacriticUrl: String?
)
