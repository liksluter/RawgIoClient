package mr.liks.feature.feed.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.model.GamePreview
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.feed.impl.data.mapper.toDomain
import mr.liks.feature.feed.impl.domain.repository.FeedRepository

/**
 * Реализация [FeedRepository]
 *
 * @property api инстанс [RawgApi]
 * @property database нистанс БД
 * @property dispatchers диспетчеры
 */
@OptIn(ExperimentalPagingApi::class)
class FeedRepositoryImpl(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val dispatchers: DispatchersProvider
) : FeedRepository {
    override fun getPagingData(): Flow<PagingData<GamePreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 3,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            remoteMediator = FeedRemoteMediator(
                api = api,
                database = database,
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = { database.gameDao().pagingSource() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { gameWithRelations -> gameWithRelations.toDomain() }
            }
            .flowOn(dispatchers.io)
    }

    override suspend fun refresh() {
        database.withTransaction {
            database.remoteKeyDao().clearAll()
            database.gameDao().clearPlatformCrossRefs()
            database.gameDao().clearGames()
            database.gameDao().clearPlatforms()
        }
    }

    override suspend fun loadNextPage() {
        // todo для будущих сценариев
    }

    private companion object {
        const val PAGE_SIZE = 30
        const val PREFETCH_DISTANCE = 5
    }
}