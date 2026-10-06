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
interface GameDao {
    /** @return PagingSource ленты по признаку сортировки [ordering] */
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

    @Upsert
    suspend fun upsertFeedEntries(entries: List<FeedEntryEntity>)

    @Query("DELETE FROM feed_entries WHERE ordering = :ordering")
    suspend fun clearFeedEntries(ordering: String)

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

    @Upsert
    suspend fun upsertSearchEntries(entries: List<SearchEntryEntity>)

    @Query("DELETE FROM search_entries WHERE query = :query")
    suspend fun clearSearchEntries(query: String)

    @Query("DELETE FROM search_entries WHERE query NOT IN (:activeQueries)")
    suspend fun clearSearchEntriesExcept(activeQueries: List<String>)

    @Query("SELECT * FROM games WHERE id = :id")
    fun observeById(id: Long): Flow<GameEntity?>

    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    fun observeWithRelations(id: Long): Flow<GameWithPropertiesRelation?>

    @Upsert
    suspend fun upsertGames(games: List<GameEntity>)

    @Upsert
    suspend fun upsertGame(game: GameEntity)

    @Query("SELECT COUNT(*) FROM games")
    suspend fun count(): Long

    @Upsert
    suspend fun upsertPlatforms(platforms: List<PlatformEntity>)

    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)

    @Upsert
    suspend fun upsertDevelopers(developers: List<DeveloperEntity>)

    @Upsert
    suspend fun upsertPublishers(publishers: List<PublisherEntity>)

    @Upsert
    suspend fun upsertPlatformCrossRefs(refs: List<GamePlatformCrossRef>)

    @Upsert
    suspend fun upsertGenreCrossRefs(refs: List<GameGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlatformCrossRefs(refs: List<GamePlatformCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreCrossRefs(refs: List<GameGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeveloperCrossRefs(refs: List<GameDeveloperCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPublisherCrossRefs(refs: List<GamePublisherCrossRef>)

    @Query("DELETE FROM game_platform_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deletePlatformRefsFor(gameIds: List<Long>)

    @Query("DELETE FROM game_genre_cross_ref WHERE gameId IN (:gameIds)")
    suspend fun deleteGenreRefsFor(gameIds: List<Long>)

    @Query("DELETE FROM games")
    suspend fun clearAllGames()

    @Query("DELETE FROM platforms")
    suspend fun clearPlatforms()

    @Query("DELETE FROM genres")
    suspend fun clearGenres()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGameIgnore(game: GameEntity): Long

    @Query(
        """
    UPDATE games SET
        slug = :slug,
        name = :name,
        released = :released,
        backgroundImage = :backgroundImage,
        rating = :rating,
        metacritic = :metacritic,
        playtime = :playtime,
        updatedAt = :updatedAt
    WHERE id = :id
    """
    )
    suspend fun updateGameFromDetails(
        id: Long,
        slug: String,
        name: String,
        released: String?,
        backgroundImage: String?,
        rating: Double,
        metacritic: Int?,
        playtime: Int?,
        updatedAt: Long
    )

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