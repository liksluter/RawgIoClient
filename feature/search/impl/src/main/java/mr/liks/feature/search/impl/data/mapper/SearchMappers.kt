package mr.liks.feature.search.impl.data.mapper

import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.SearchEntryEntity
import mr.liks.core.database.entity.SearchHistoryEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.model.GamePreview
import mr.liks.core.model.Genre
import mr.liks.core.model.PlatformIcon
import mr.liks.core.model.SearchHistoryItem
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.GenreDto
import mr.liks.core.network.api.dto.PlatformDto

fun GameListDto.toEntity(): GameEntity = GameEntity(
    id = id,
    slug = slug,
    name = name,
    released = released,
    backgroundImage = backgroundImage,
    rating = rating,
    ratingsCount = ratingsCount,
    metacritic = metacritic,
    playtime = playtime
)

/** DTO → запись результата поиска для запроса [query] на позиции [position]. */
fun GameListDto.toSearchEntry(query: String, position: Int): SearchEntryEntity =
    SearchEntryEntity(
        query = query,
        gameId = id,
        position = position
    )

fun PlatformDto.toEntity(): PlatformEntity = PlatformEntity(
    id = id,
    name = name,
    slug = slug,
    image = image
)

/** Маппер [GenreDto] (сеть) -> [GenreEntity] (БД) */
fun GenreDto.toEntity(): GenreEntity = GenreEntity(
    id = id,
    name = name,
    slug = slug,
    gamesCount = gamesCount
)

fun GameWithPropertiesRelation.toDomain(): GamePreview = GamePreview(
    id = game.id,
    name = game.name,
    released = game.released,
    backgroundImage = game.backgroundImage,
    rating = game.rating,
    platforms = platforms.map { it.toDomain() },
    platformsNames = platforms.takeIf{ it.isNotEmpty() }?.joinToString(separator = ", ") { it.name },
    genres = genres.takeIf { it.isNotEmpty() }?.joinToString(separator = ", ") { it.name },
    trailerUrl = null,
    trailerPreview = null
)

fun PlatformEntity.toDomain(): PlatformIcon = PlatformIcon(
    id = id,
    name = name,
    image = image
)

fun SearchHistoryEntity.toDomain(): SearchHistoryItem = SearchHistoryItem(
    id = id,
    query = query,
    searchedAt = searchedAt
)

/** Маппер [GenreEntity] в domain модель [Genre] */
fun GenreEntity.toDomain(): Genre = Genre(id = id, name = name)