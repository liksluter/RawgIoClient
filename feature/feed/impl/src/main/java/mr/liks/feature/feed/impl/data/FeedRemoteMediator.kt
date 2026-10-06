package mr.liks.feature.feed.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.RemoteKeyEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.SortOrder
import mr.liks.feature.feed.impl.data.mapper.toEntity
import mr.liks.feature.feed.impl.data.mapper.toFeedEntry
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Посредник между Paging3 и сетью
 *
 * @property api контракт API [RawgApi]
 * @property database инстанс БД
 * @property ordering признак сортировки
 * @property pageSize размер страницы
 */
@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val sortOrder: SortOrder,
    private val reverseSortOrder: Boolean,
    private val pageSize: Int,
) : RemoteMediator<Int, GameWithPropertiesRelation>() {
    private val ordering = sortOrder.getOrdering(reverseSortOrder)

    override suspend fun initialize(): InitializeAction {
        val timeout = TimeUnit.HOURS.toMillis(1)
        val last = database.remoteKeyDao().insertedAt(ordering)
        return if (last == null || System.currentTimeMillis() - last >= timeout)
            InitializeAction.LAUNCH_INITIAL_REFRESH
        else
            InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameWithPropertiesRelation>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastKey = database.remoteKeyDao().lastRemoteKey(ordering)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastKey.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response = api.getGames(
                page = page,
                pageSize = pageSize,
                sortOrder = sortOrder,
                reverseSortOrder = reverseSortOrder
            )
            val endOfPaginationReached = response.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().clearForOrdering(ordering)
                    database.gameDao().clearFeedEntries(ordering)
                }

                database.gameDao().upsertGames(response.results.map { it.toEntity() })
                database.gameDao().upsertFeedEntries(
                    response.results.map { it.toFeedEntry(ordering) }
                )

                val platforms = response.results
                    .flatMap { it.platforms }
                    .map { it.platform.toEntity() }
                    .distinctBy { it.id }
                if (platforms.isNotEmpty()) {
                    database.gameDao().upsertPlatforms(platforms)
                }

                val refs = response.results.flatMap { game ->
                    game.platforms.map { wrapper ->
                        GamePlatformCrossRef(game.id, wrapper.platform.id, wrapper.releasedAt)
                    }
                }
                if (refs.isNotEmpty()) {
                    database.gameDao().upsertPlatformCrossRefs(refs)
                }

                val genres = response.results
                    .flatMap { it.genres }
                    .map { it.toEntity() }
                    .distinctBy { it.id }
                if (genres.isNotEmpty()) {
                    database.gameDao().upsertGenres(genres)
                }

                val genreCrossRefs = response.results.flatMap { game ->
                    game.genres.map { genre ->
                        GameGenreCrossRef(game.id, genre.id)
                    }
                }
                if (genreCrossRefs.isNotEmpty()) {
                    database.gameDao().upsertGenreCrossRefs(genreCrossRefs)
                }

                database.remoteKeyDao().insertKey(
                    RemoteKeyEntity(
                        ordering = ordering,
                        prevPage = if (page <= 1) null else page - 1,
                        nextPage = if (endOfPaginationReached) null else page + 1,
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (t: Throwable) {
            Timber.w(t, "FeedRemoteMediator failed at page %d, type %s", page, loadType)
            MediatorResult.Error(t)
        }
    }
}