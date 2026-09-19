package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.Index

/**
 * Многие ко многим `games` - `genres`
 *
 * @property gameId id игры
 * @property genreId id жанра
 */
@Entity(
    tableName = "game_genre_cross_ref",
    primaryKeys = ["gameId", "genreId"],
    indices = [Index("genreId")]
)
data class GameGenreCrossRef(
    val gameId: Long,
    val genreId: Long
)
