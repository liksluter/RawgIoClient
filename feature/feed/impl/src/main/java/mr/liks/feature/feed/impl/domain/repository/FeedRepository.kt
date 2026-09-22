package mr.liks.feature.feed.impl.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mr.liks.core.model.GamePreview

/** Контракт репозитория ленты */
interface FeedRepository {

    /** @return поток страниц ленты [GamePreview] */
    fun getPagingData(): Flow<PagingData<GamePreview>>

    /** Force рефреш */
    suspend fun refresh()

    /** Загрузка следующей страницы */
    suspend fun loadNextPage()
}