package mr.liks.feature.search.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.search.impl.data.mapper.toEntity
import mr.liks.feature.search.impl.data.mapper.toSearchEntry
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
    private val query: String,
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val pageSize: Int
) : RemoteMediator<Int, GameWithPropertiesRelation>() {

    override suspend fun initialize(): InitializeAction =
        InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameWithPropertiesRelation>
    ): MediatorResult {
        if (query.isBlank()) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }
        if (loadType != LoadType.REFRESH) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val response = api.searchGames(
                query = query,
                page = 1,
                pageSize = pageSize
            )

            database.withTransaction {
                database.gameDao().upsertGames(response.results.map { it.toEntity() })

                database.gameDao().clearSearchEntries(query)
                database.gameDao().upsertSearchEntries(
                    response.results.mapIndexed { index, dto ->
                        dto.toSearchEntry(query, index)
                    }
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
                    database.gameDao().insertPlatformCrossRefs(refs)
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
                    database.gameDao().insertGenreCrossRefs(genreCrossRefs)
                }
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (t: Throwable) {
            Timber.w(t, "SearchRemoteMediator failed for '%s'", query)
            MediatorResult.Error(t)
        }
    }
}