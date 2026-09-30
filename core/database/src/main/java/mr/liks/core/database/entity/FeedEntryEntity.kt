package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Запись о том, что игра находится в ленте при конкретном `ordering`
 *
 * @property ordering порядок сортировки
 * @property gameId id гры
 * @property sortValue значение для сортировки в зависимости от `ordering`
 * @property secondarySort обеспечивает стабильность при одинаковых `sortValue`
 */
@Entity(
    tableName = "feed_entries",
    primaryKeys = ["ordering", "gameId"],
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ordering"), Index("gameId")]
)
data class FeedEntryEntity(
    val ordering: String,
    val gameId: Long,
    val sortValue: Double,
    val secondarySort: Long
)