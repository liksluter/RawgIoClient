package mr.liks.core.database.entity

import androidx.room.Entity

/**
 * Ключи страниц для каждого элемента, таблица `feed_remote_keys`
 *
 * @property ordering признак сортировки
 * @property prevPage следующая страница
 * @property nextPage предыдущая страница
 * @property insertedAt время вставки
 */
@Entity(tableName = "feed_remote_keys", primaryKeys = ["ordering"])
data class RemoteKeyEntity(
    val ordering: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val insertedAt: Long = System.currentTimeMillis()
)
