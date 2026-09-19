package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Многие ко многим `games` - `publishers`
 *
 * @property gameId id игры
 * @property publisherId id издателя
 */
@Entity(
    tableName = "game_publisher_cross_ref",
    primaryKeys = ["gameId", "publisherId"],
    indices = [Index("publisherId")]
)
data class GamePublisherCrossRef(
    val gameId: Long,
    val publisherId: Long
)
