package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность игры в таблице `games`
 *
 * @property id id
 * @property slug слаг
 * @property name название
 * @property released дата релиза
 * @property backgroundImage фоновое изображение
 * @property rating рейтинг
 * @property ratingsCount кол-во голосов в рейтинге
 * @property metacritic рейтинг метакритика
 * @property playtime время игры
 * @property feedOrder признак для сортировки
 * @property updatedAt время последнего обновления записи
 */
@Entity(
    tableName = "games",
    indices = [
        Index("slug"),
        Index("name"),
        Index("feedOrder")
    ]
)
data class GameEntity(
    @PrimaryKey val id: Long,
    val slug: String,
    val name: String,
    val released: String?,
    val backgroundImage: String?,
    val rating: Double,
    val ratingsCount: Int,
    val metacritic: Int?,
    val playtime: Int?,
    val feedOrder: Long,
    val updatedAt: Long = System.currentTimeMillis()
)
