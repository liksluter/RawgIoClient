package mr.liks.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation

/** DAO для работы с играми, платформами, жанрами и связями */
@Dao
interface GameDao {
    /** @return [PagingSource] для ленты игр */
    @Transaction
    @Query(
        """
        SELECT * FROM games
        ORDER BY feedOrder ASC
        """
    )
    fun pagingSource(): PagingSource<Int, GameWithPropertiesRelation>

    /** @return [PagingSource] по поисковому запросу [query] */
    @Query(
        """
        SELECT * FROM games
        WHERE name LIKE '%' || :query || '%'
        ORDER BY rating DESC, id DESC
        """
    )
    fun searchPagingSource(query: String): PagingSource<Int, GameEntity>

    /** @return [Flow] для наблюдения за одной игрой с идентфиикатором [id] */
    @Query("SELECT * FROM games WHERE id = :id")
    fun observeById(id: Long): Flow<GameEntity?>

    /** @return [Flow] для наблюдения за связью [GameWithPropertiesRelation] по идентификатору [id] */
    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    fun observeWithRelations(id: Long): Flow<GameWithPropertiesRelation?>

    /** Вставляет/обновляет список игр из [games] */
    @Upsert
    suspend fun upsertGames(games: List<GameEntity>)

    /** Вставляет/обновляет игру */
    @Upsert
    suspend fun upsertGame(game: GameEntity)

    /** Вставляет/обновляет список платформ из [platforms] */
    @Upsert
    suspend fun upsertPlatforms(platforms: List<PlatformEntity>)

    /** Вставляет/обновляет список жанров из [genres] */
    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)

    /** Вставляет [refs] в [GamePlatformCrossRef] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlatformCrossRefs(refs: List<GamePlatformCrossRef>)

    /** Вставляет [refs] в [GameGenreCrossRef] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreCrossRefs(refs: List<GameGenreCrossRef>)

    /** Удаляет из [GamePlatformCrossRef] на основе [gameIds] */
    @Query("DELETE FROM game_platform_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deletePlatformRefsFor(gameIds: List<Long>)

    /** Удаляет из [GameGenreCrossRef] на основе [gameIds] */
    @Query("DELETE FROM game_genre_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deleteGenreRefsFor(gameIds: List<Long>)

    /** Очищает таблицу `games` */
    @Query("DELETE FROM games")
    suspend fun clearGames()

    /** Очищает таблицу `platforms` */
    @Query("DELETE FROM platforms")
    suspend fun clearPlatforms()

    /** Очищает таблицу `genres` */
    @Query("DELETE FROM genres")
    suspend fun clearGenres()

    /** Очищает таблицу `game_platform_cross_ref` */
    @Query("DELETE FROM game_platform_cross_ref")
    suspend fun clearPlatformCrossRefs()

    /** Очищает таблицу `game_genre_cross_ref` */
    @Query("DELETE FROM game_genre_cross_ref")
    suspend fun clearGenreCrossRefs()

    /** @return количество игр в `games` */
    @Query("SELECT COUNT(*) FROM games")
    suspend fun count(): Long

    /** @return id игры с максимальным feedOrder или null, если игр нет */
    @Query("SELECT id FROM games ORDER BY feedOrder DESC LIMIT 1")
    suspend fun lastFeedGameId(): Long?
}