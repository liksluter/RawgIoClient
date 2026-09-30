package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Результат поиска по конкретному запросу
 *
 * @property query запрос
 * @property gameId id игры
 * @property position индекс в выдаче API
 */
@Entity(
    tableName = "search_entries",
    primaryKeys = ["query", "gameId"],
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("query"), Index("gameId")]
)
data class SearchEntryEntity(
    val query: String,
    val gameId: Long,
    val position: Int
)