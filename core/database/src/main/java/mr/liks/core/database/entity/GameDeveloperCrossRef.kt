package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Многие ко многим `developers` - `games`
 *
 * @property gameId id игры
 * @property developerId id разработчика
 */
@Entity(
    tableName = "game_developer_cross_ref",
    primaryKeys = ["gameId", "developerId"],
    indices = [Index("developerId")]
)
data class GameDeveloperCrossRef(
    val gameId: Long,
    val developerId: Long
)
