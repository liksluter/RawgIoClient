package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность разработчика в таблице `developers`
 *
 * @property id id
 * @property name название
 * @property slug слаг
 */
@Entity(tableName = "developers")
data class DeveloperEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val slug: String
)
