package mr.liks.feature.details.impl.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.entity.GameDeveloperCrossRef
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GamePublisherCrossRef
import mr.liks.core.model.GameDetails
import mr.liks.core.model.GameMedia
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.details.impl.data.mapper.toDomain
import mr.liks.feature.details.impl.data.mapper.toEntity
import mr.liks.feature.details.impl.domain.repository.DetailsRepository
import timber.log.Timber

/**
 * Реализация [DetailsRepository]
 *
 * @property api инстанс [RawgApi]
 * @property database инстанс БД
 * @property dispatchers диспетчеры
 */
class DetailsRepositoryImpl(
    private val api: RawgApi,
    private val database: RawgDatabase,
    private val dispatchers: DispatchersProvider
) : DetailsRepository {
    override fun observeDetails(gameId: Long): Flow<GameDetails?> {
        return combine(
            database.gameDao().observeById(gameId),
            database.gameDetailsDao().observeDetails(gameId),
            database.gameDao().observeWithRelations(gameId)
        ) { game, details, relations ->
            if (game == null || details == null) return@combine null

            GameDetails(
                id = game.id,
                name = game.name,
                backgroundImage = game.backgroundImage,
                description = details.description,
                descriptionRaw = details.descriptionRaw,
                released = game.released,
                rating = game.rating,
                metacritic = game.metacritic,
                playtime = game.playtime,
                website = details.website,
                redditUrl = details.redditUrl,
                metacriticUrl = details.metacriticUrl,
                developers = relations?.let { r ->
                    emptyList()
                } ?: emptyList(),
                publishers = emptyList(),
                genres = relations?.genres?.map { it.toDomain() } ?: emptyList(),
                platforms = relations?.platforms?.map { it.toDomain() } ?: emptyList()
            )
        }
            .flowOn(dispatchers.io)
    }

    override fun observeMedia(gameId: Long): Flow<GameMedia> {
        return combine(
            database.gameDetailsDao().observeTrailers(gameId),
            database.gameDetailsDao().observeScreenshots(gameId)
        ) { trailers, screenshots ->
            GameMedia(
                trailers = trailers.map { it.toDomain() },
                screenshots = screenshots.map { it.toDomain() }
            )
        }
            .flowOn(dispatchers.io)
    }

    override suspend fun refreshDetails(gameId: Long) {
        withContext(dispatchers.io) {
            try {
                val dto = api.getGameDetails(gameId = gameId)

                database.withTransaction {
                    database.gameDao().upsertGamePreservingFeedOrder(
                        GameEntity(
                            id = dto.id,
                            slug = dto.slug,
                            name = dto.name,
                            released = dto.released,
                            backgroundImage = dto.backgroundImage,
                            rating = dto.rating,
                            ratingsCount = 0,
                            metacritic = dto.metacritic,
                            playtime = dto.playtime,
                            feedOrder = -1
                        )
                    )

                    database.gameDetailsDao().upsertDetails(dto.toEntity())

                    val developers = dto.developers.map { it.toEntity() }
                    if (developers.isNotEmpty()) {
                        database.gameDao().upsertDevelopers(developers)
                        database.gameDao().insertDeveloperCrossRefs(
                            developers.map { GameDeveloperCrossRef(gameId, it.id) }
                        )
                    }

                    val publishers = dto.publishers.map { it.toEntity() }
                    if (publishers.isNotEmpty()) {
                        database.gameDao().upsertPublishers(publishers)
                        database.gameDao().insertPublisherCrossRefs(
                            publishers.map { GamePublisherCrossRef(gameId, it.id) }
                        )
                    }

                    val genres = dto.genres.map { it.toEntity() }
                    if (genres.isNotEmpty()) {
                        database.gameDao().upsertGenres(genres)
                        database.gameDao().insertGenreCrossRefs(
                            genres.map { GameGenreCrossRef(gameId, it.id) }
                        )
                    }

                    val platforms = dto.platforms.map { it.platform.toEntity() }
                    if (platforms.isNotEmpty()) {
                        database.gameDao().upsertPlatforms(platforms)
                        database.gameDao().insertPlatformCrossRefs(
                            dto.platforms.map {
                                GamePlatformCrossRef(gameId, it.platform.id, it.releasedAt)
                            }
                        )
                    }
                }
            } catch (t: Throwable) {
                Timber.w(t, "Failed to refresh details for game %d", gameId)
                throw t
            }
        }
    }

    override suspend fun refreshMedia(gameId: Long) {
        withContext(dispatchers.io) {
            try {
                val movies = runCatching {
                    api.getGameTrailers(gameId = gameId)
                }.getOrNull()

                val screenshots = runCatching {
                    api.getGameScreenshots(gameId = gameId)
                }.getOrNull()

                database.withTransaction {
                    if (movies != null) {
                        database.gameDetailsDao().clearTrailers(gameId)
                        database.gameDetailsDao().upsertTrailers(
                            movies.results.map { it.toEntity(gameId) }
                        )
                    }
                    if (screenshots != null) {
                        database.gameDetailsDao().clearScreenshots(gameId)
                        database.gameDetailsDao().upsertScreenshots(
                            screenshots.results.map { it.toEntity(gameId) }
                        )
                    }
                }
            } catch (t: Throwable) {
                Timber.w(t, "Failed to refresh media for game %d", gameId)
                throw t
            }
        }
    }
}