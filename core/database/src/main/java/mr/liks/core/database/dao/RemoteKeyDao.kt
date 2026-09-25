package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mr.liks.core.database.entity.RemoteKeyEntity

/** DAO для Paging 3 remote keys */
@Dao
interface RemoteKeyDao {
    /** @return ключи для игры [RemoteKeyEntity] по идентификатору id */
    @Query("SELECT * FROM remote_keys WHERE gameId = :id")
    suspend fun remoteKeyById(id: Long): RemoteKeyEntity?

    /** Вставляет список [keys] в `remote_keys` */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeyEntity>)

    /** Вставляет ключ страницы [key] в `remote_keys` */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: RemoteKeyEntity)

    /** Очищает таблицу `remote_keys` */
    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

    @Query("SELECT MAX(insertedAt) FROM remote_keys")
    suspend fun maxInsertedAt(): Long?
}