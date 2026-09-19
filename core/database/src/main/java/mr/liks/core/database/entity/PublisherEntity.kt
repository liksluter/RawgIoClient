package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность издателя, таблица `publishers`
 *
 * @property id id
 * @property name название
 * @property slug слаг
 */
@Entity(tableName = "publishers")
data class PublisherEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val slug: String
)
