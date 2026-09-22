package mr.liks.feature.feed.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.entity.RemoteKeyEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.feed.impl.data.mapper.platformCrossRef
import mr.liks.feature.feed.impl.data.mapper.toEntity
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Посредник между Paging3 и сетью
 *
 * @property api контракт API [RawgApi]
 * @property database инстанс БД
 * @property pageSize размер страницы
 */
@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediator(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val pageSize: Int
) : RemoteMediator<Int, GameWithPropertiesRelation>() {

    override suspend fun initialize(): InitializeAction {
        val timeout = TimeUnit.HOURS.toMillis(1)
        val last = database.remoteKeyDao().maxInsertedAt()
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
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                if (remoteKeys == null) {
                    val dbHasData = database.gameDao().count() > 0
                    return MediatorResult.Success(endOfPaginationReached = !dbHasData)
                }

                remoteKeys.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response = api.getGames(
                page = page,
                pageSize = pageSize,
                ordering = ORDERING
            )
            val endOfPaginationReached = response.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeyDao().clearAll()
                    database.gameDao().clearGames()
                    database.gameDao().clearPlatforms()
                }

                database.gameDao().upsertGames(response.results.mapIndexed {  index, dto ->
                    val feedOrder = (page - 1L) * pageSize + index
                    dto.toEntity(feedOrder)
                })

                val platforms = response.results
                    .flatMap { it.platforms }
                    .map { it.platform.toEntity() }
                    .distinctBy { it.id }
                if (platforms.isNotEmpty()) {
                    database.gameDao().upsertPlatforms(platforms)
                }

                val refs = response.results.flatMap { game ->
                    game.platforms.map { wrapper ->
                        platformCrossRef(game.id, wrapper.platform.id, wrapper.releasedAt)
                    }
                }
                if (refs.isNotEmpty()) {
                    database.gameDao().insertPlatformCrossRefs(refs)
                }

                val remoteKeys = response.results.map { game ->
                    RemoteKeyEntity(
                        gameId = game.id,
                        prevPage = if (page <= 1) null else page - 1,
                        nextPage = if (endOfPaginationReached) null else page + 1
                    )
                }
                database.remoteKeyDao().insertAll(remoteKeys)
            }

            MediatorResult.Success(endOfPaginationReached)
        } catch (t: Throwable) {
            Timber.w(t, "FeedRemoteMediator failed at page %d, type %s", page, loadType)
            MediatorResult.Error(t)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, GameWithPropertiesRelation>
    ): RemoteKeyEntity? {
        val fromState = state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.game?.id

        val id = fromState ?: database.gameDao().lastFeedGameId() ?: return null

        return database.remoteKeyDao().remoteKeyById(id)
    }

    private companion object {
        const val ORDERING = "-added"
    }
}