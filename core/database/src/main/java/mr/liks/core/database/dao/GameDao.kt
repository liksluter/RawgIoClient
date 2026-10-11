package mr.liks.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import mr.liks.core.database.entity.DeveloperEntity
import mr.liks.core.database.entity.FeedEntryEntity
import mr.liks.core.database.entity.GameDeveloperCrossRef
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GamePublisherCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.PublisherEntity
import mr.liks.core.database.entity.SearchEntryEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation

/** DAO для работы с играми, платформами, жанрами и связями */
@Dao
interface GameDao : InternalGameDao {
    /** @return [PagingSource] ленты по признаку сортировки [ordering] */
    @Transaction
    @Query(
        """
        SELECT g.* FROM games g
        INNER JOIN feed_entries f ON f.gameId = g.id
        WHERE f.ordering = :ordering
        ORDER BY f.sortValue ASC, f.secondarySort ASC
        """
    )
    fun feedPagingSource(ordering: String): PagingSource<Int, GameWithPropertiesRelation>

    /** Апсерт списка ленты [entries] */
    @Upsert
    suspend fun upsertFeedEntries(entries: List<FeedEntryEntity>)

    /** Удаляет строки из `feed_entries` на основе [ordering] */
    @Query("DELETE FROM feed_entries WHERE ordering = :ordering")
    suspend fun clearFeedEntries(ordering: String)

    /** @return [PagingSource] ленты результатов поиска по запросу [query] */
    @Transaction
    @Query(
        """
        SELECT g.* FROM games g
        INNER JOIN search_entries s ON s.gameId = g.id
        WHERE s.query = :query
        ORDER BY s.position ASC
        """
    )
    fun searchPagingSource(query: String): PagingSource<Int, GameWithPropertiesRelation>

    /** Апсерт списка запросов поиска [entries] */
    @Upsert
    suspend fun upsertSearchEntries(entries: List<SearchEntryEntity>)

    /** Удаляет строки из `search_entries` на основе [query] */
    @Query("DELETE FROM search_entries WHERE query = :query")
    suspend fun clearSearchEntries(query: String)

    /** @return флоу [GameEntity] по [id] игры  */
    @Query("SELECT * FROM games WHERE id = :id")
    fun observeById(id: Long): Flow<GameEntity?>

    /** @return флоу [GameWithPropertiesRelation] по [id] игры  */
    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    fun observeWithRelations(id: Long): Flow<GameWithPropertiesRelation?>

    /** Апсерт списка игр [games] */
    @Upsert
    suspend fun upsertGames(games: List<GameEntity>)

    /** Апсерт информации об игре [game] */
    @Upsert
    suspend fun upsertGame(game: GameEntity)

    /** @return кол-во игр */
    @Query("SELECT COUNT(*) FROM games")
    suspend fun count(): Long

    /** Апсерт списка платформ [platforms] */
    @Upsert
    suspend fun upsertPlatforms(platforms: List<PlatformEntity>)

    /** Апсерт списка жанров [genres] */
    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)

    /** Апсерт списка разработчиков [developers] */
    @Upsert
    suspend fun upsertDevelopers(developers: List<DeveloperEntity>)

    /** Апсерт списка издателей [publishers] */
    @Upsert
    suspend fun upsertPublishers(publishers: List<PublisherEntity>)

    /** Апсерт списка связей игра платформа [refs] */
    @Upsert
    suspend fun upsertPlatformCrossRefs(refs: List<GamePlatformCrossRef>)

    /** Апсерт списка связей игра жанр [refs] */
    @Upsert
    suspend fun upsertGenreCrossRefs(refs: List<GameGenreCrossRef>)

    /** Вставка списка связей игра платформа [refs] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlatformCrossRefs(refs: List<GamePlatformCrossRef>)

    /** Вставка списка связей игра жанр [refs] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreCrossRefs(refs: List<GameGenreCrossRef>)

    /** Вставка списка связей игра разработчик [refs] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeveloperCrossRefs(refs: List<GameDeveloperCrossRef>)

    /** Вставка списка связей игра издатель [refs] */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublisherCrossRefs(refs: List<GamePublisherCrossRef>)

    /** Удаляет строки из `game_platform_cross_ref` по id из списка [gameIds] */
    @Query("DELETE FROM game_platform_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deletePlatformRefsFor(gameIds: List<Long>)

    /** Удаляет строки из `game_genre_cross_ref` по id из списка [gameIds] */
    @Query("DELETE FROM game_genre_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deleteGenreRefsFor(gameIds: List<Long>)

    /** Очищает таблицу `games` */
    @Query("DELETE FROM games")
    suspend fun clearAllGames()

    /** Очищает таблицу `platforms` */
    @Query("DELETE FROM platforms")
    suspend fun clearPlatforms()

    /** Очищает таблицу `genres` */
    @Query("DELETE FROM genres")
    suspend fun clearGenres()

    /** @return -1, если запись уже существует, иначе id вставленной записи [game] */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGameIgnore(game: GameEntity): Long

    /** Апсерт игры [game] */
    @Transaction
    suspend fun upsertGameFromDetails(game: GameEntity) {
        val rowId = insertGameIgnore(game)
        if (rowId == -1L) {
            updateGameFromDetails(
                id = game.id,
                slug = game.slug,
                name = game.name,
                released = game.released,
                backgroundImage = game.backgroundImage,
                rating = game.rating,
                metacritic = game.metacritic,
                playtime = game.playtime,
                updatedAt = game.updatedAt
            )
        }
    }
}