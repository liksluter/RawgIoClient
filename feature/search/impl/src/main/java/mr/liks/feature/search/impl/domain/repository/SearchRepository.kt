package mr.liks.feature.search.impl.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem

/** Контракт поиска */
interface SearchRepository {
    fun search(query: String): Flow<PagingData<GamePreview>>

    fun observeHistory(): Flow<List<SearchHistoryItem>>

    suspend fun saveQuery(query: String)

    suspend fun deleteQuery(query: String)

    suspend fun clearHistory()
}