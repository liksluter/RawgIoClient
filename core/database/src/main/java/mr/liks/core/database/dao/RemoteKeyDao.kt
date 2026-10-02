package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mr.liks.core.database.entity.RemoteKeyEntity

/** DAO для Paging 3 remote keys */
@Dao
interface RemoteKeyDao {
    @Query("""
        SELECT * FROM feed_remote_keys
        WHERE ordering = :ordering
        LIMIT 1
    """)
    suspend fun lastRemoteKey(ordering: String): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: RemoteKeyEntity)

    @Query("DELETE FROM feed_remote_keys WHERE ordering = :ordering")
    suspend fun clearForOrdering(ordering: String)

    @Query("SELECT insertedAt FROM feed_remote_keys WHERE ordering = :ordering")
    suspend fun insertedAt(ordering: String): Long?
}