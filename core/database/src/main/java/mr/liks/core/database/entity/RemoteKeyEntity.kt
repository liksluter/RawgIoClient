package mr.liks.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Ключи страниц для каждого элемента, таблица `remote_keys`
 *
 * @property gameId id id игры
 * @property prevPage следующая страница
 * @property nextPage предыдущая страница
 */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val gameId: Long,
    val prevPage: Int?,
    val nextPage: Int?
)
