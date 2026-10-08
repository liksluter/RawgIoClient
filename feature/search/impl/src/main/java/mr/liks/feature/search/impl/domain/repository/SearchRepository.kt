package mr.liks.feature.search.impl.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem

/** Контракт поиска */
interface SearchRepository {
    /** @return поток страниц ленты [GamePreview] на основе запроса [query] */
    fun search(query: String): Flow<PagingData<GamePreview>>

    /** @return поток списка истории поиска [SearchHistoryItem] */
    fun observeHistory(): Flow<List<SearchHistoryItem>>

    /** Сохранение запрос поиска [query] */
    suspend fun saveQuery(query: String)

    /** Удаление запроса поиска [query] */
    suspend fun deleteQuery(query: String)

    /** Удаление всей истории поиска */
    suspend fun clearHistory()
}