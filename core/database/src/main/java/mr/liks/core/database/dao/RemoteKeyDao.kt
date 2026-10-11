package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mr.liks.core.database.entity.RemoteKeyEntity

/** DAO для Paging 3 remote keys */
@Dao
interface RemoteKeyDao {
    /** @return ключ страниц [RemoteKeyEntity] для [ordering] */
    @Query("""
        SELECT * FROM feed_remote_keys
        WHERE ordering = :ordering
        LIMIT 1
    """)
    suspend fun lastRemoteKey(ordering: String): RemoteKeyEntity?

    /** Вставляет [key] в `feed_remote_keys` */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: RemoteKeyEntity)

    /** Удаляет из `feed_remote_keys` записи для [ordering] */
    @Query("DELETE FROM feed_remote_keys WHERE ordering = :ordering")
    suspend fun clearForOrdering(ordering: String)

    /** @return время вставки последней записи ключа для [ordering] */
    @Query("SELECT insertedAt FROM feed_remote_keys WHERE ordering = :ordering")
    suspend fun insertedAt(ordering: String): Long?
}