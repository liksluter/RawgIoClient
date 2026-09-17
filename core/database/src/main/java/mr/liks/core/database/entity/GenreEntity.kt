package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность жанра, таблица `genres`
 *
 * @property id id
 * @property name название
 * @property slug слаг
 */
@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val slug: String
)
