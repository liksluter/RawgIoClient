package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import mr.liks.core.database.entity.SearchHistoryEntity

/** DAO для истории поиска */
@Dao
interface SearchHistoryDao {
    /** @return [Flow] для наблюдения за списком поисковых запросов [SearchHistoryEntity] с лимитом [limit] */
    @Query("SELECT * FROM search_history ORDER BY searchedAt DESC LIMIT :limit")
    fun observeHistory(limit: Int = 20): Flow<List<SearchHistoryEntity>>

    /** Вставляет/обновляет поисковый зарос [entry] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: SearchHistoryEntity)

    /** Удаляет записи из `search_history` на основе запроса [query] */
    @Query("DELETE FROM search_history WHERE query = :query")
    suspend fun deleteByQuery(query: String)

    /** Очищает таблицу `search_history` */
    @Query("DELETE FROM search_history")
    suspend fun clearAll()

    @Query(
        """
        DELETE FROM search_history
        WHERE id NOT IN (
            SELECT id FROM search_history ORDER BY searchedAt DESC LIMIT :keep
        )
        """
    )
    suspend fun trimTo(keep: Int)
}