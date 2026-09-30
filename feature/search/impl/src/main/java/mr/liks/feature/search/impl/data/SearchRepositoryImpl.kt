package mr.liks.feature.search.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.entity.SearchHistoryEntity
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.search.impl.data.mapper.toDomain
import mr.liks.feature.search.impl.domain.repository.SearchRepository

@OptIn(ExperimentalPagingApi::class)
class SearchRepositoryImpl(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val dispatchers: DispatchersProvider
) : SearchRepository {
    override fun search(query: String): Flow<PagingData<GamePreview>> {
        if (query.isBlank()) return flowOf(PagingData.empty())

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = SearchRemoteMediator(
                query = query,
                api = api,
                database = database,
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = { database.gameDao().searchPagingSource(query) }
        )
            .flow
            .map { pagingData ->
                pagingData.map { gameWithRelations -> gameWithRelations.toDomain() }
            }
            .flowOn(dispatchers.io)
    }

    override fun observeHistory(): Flow<List<SearchHistoryItem>> =
        database.searchHistoryDao()
            .observeHistory(limit = HISTORY_LIMIT)
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(dispatchers.io)

    override suspend fun saveQuery(query: String) {
        withContext(dispatchers.io) {
            database.withTransaction {
                database.searchHistoryDao().upsert(SearchHistoryEntity(query = query))
                database.searchHistoryDao().trimTo(HISTORY_LIMIT)
            }
        }
    }

    override suspend fun deleteQuery(query: String) {
        withContext(dispatchers.io) {
            database.withTransaction {
                database.searchHistoryDao().deleteByQuery(query)
                database.gameDao().clearSearchEntries(query)
            }
        }
    }

    override suspend fun clearHistory() {
        withContext(dispatchers.io) {
            database.withTransaction {
                database.searchHistoryDao().clearAll()
                database.gameDao().clearSearchEntriesExcept(emptyList())
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 60
        const val HISTORY_LIMIT = 60
    }
}