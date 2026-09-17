package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность платформы в таблице `platforms`
 *
 * @property id id
 * @property name название
 * @property slug слаг
 * @property imageBackground лого платформы
 */
@Entity(tableName = "platforms")
data class PlatformEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val slug: String,
    val imageBackground: String?
)
