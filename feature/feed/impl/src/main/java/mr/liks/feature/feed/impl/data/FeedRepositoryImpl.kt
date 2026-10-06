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
import kotlinx.coroutines.withContext
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.model.GamePreview
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.SortOrder
import mr.liks.feature.feed.impl.data.mapper.toDomain
import mr.liks.feature.feed.impl.domain.repository.FeedRepository

/**
 * Реализация [FeedRepository]
 *
 * @property api инстанс [RawgApi]
 * @property database нистанс БД
 * @property dispatchers диспетчеры
 * @property sortOrder порядок сортировки
 * @property reverseSortOrder направление сортировки, `true` обратный порядок
 */
@OptIn(ExperimentalPagingApi::class)
class FeedRepositoryImpl(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val dispatchers: DispatchersProvider,
    private val sortOrder: SortOrder = SortOrder.ADDED,
    private val reverseSortOrder: Boolean = true
) : FeedRepository {
    private val ordering = sortOrder.getOrdering(reverseSortOrder)

    override fun getPagingData(): Flow<PagingData<GamePreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = true
            ),
            remoteMediator = FeedRemoteMediator(
                api = api,
                database = database,
                sortOrder = sortOrder,
                reverseSortOrder = reverseSortOrder,
                pageSize = PAGE_SIZE,
            ),
            pagingSourceFactory = { database.gameDao().feedPagingSource(ordering) }
        )
            .flow
            .map { pagingData ->
                pagingData.map { gameWithRelations ->
                    gameWithRelations.toDomain()
                }
            }
            .flowOn(dispatchers.io)
    }

    override suspend fun refresh() {
        withContext(dispatchers.io) {
            database.withTransaction {
                database.remoteKeyDao().clearForOrdering(ordering)
                database.gameDao().clearFeedEntries(ordering)
            }
        }
    }

    override suspend fun loadNextPage() { }

    private companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }
}