package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Многие ко многим `games` - `platforms`
 *
 * @property gameId id игры
 * @property platformId id платформы
 * @property releasedAt дата релиза
 */
@Entity(
    tableName = "game_platform_cross_ref",
    primaryKeys = ["gameId", "platformId"],
    indices = [Index("platformId")]
)
data class GamePlatformCrossRef(
    val gameId: Long,
    val platformId: Long,
    val releasedAt: String?
)
