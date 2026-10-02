package mr.liks.feature.feed.impl.data.mapper

import mr.liks.core.database.entity.FeedEntryEntity
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.model.GamePreview
import mr.liks.core.model.PlatformIcon
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.PlatformDto
import java.time.Instant

/** Маппер [GameListDto] -> [GameEntity] */
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

/** Маппер [GameListDto] -> [FeedEntryEntity] с признаком сортировки [ordering] */
fun GameListDto.toFeedEntry(ordering: String): FeedEntryEntity {
    val sortValue = when (ordering) {
        "-rating" -> -rating
        "-metacritic" -> -(metacritic ?: 0).toDouble()
        "-released" -> -parseIsoToEpochSeconds(released).toDouble()
        else -> -rating
    }
    return FeedEntryEntity(
        ordering = ordering,
        gameId = id,
        sortValue = sortValue,
        secondarySort = id
    )
}

/** Маппер [PlatformDto] -> [PlatformEntity] */
fun PlatformDto.toEntity(): PlatformEntity = PlatformEntity(
    id = id,
    name = name,
    slug = slug,
    image = null
)

/** Маппер [GameWithPropertiesRelation] -> [GamePreview] */
fun GameWithPropertiesRelation.toDomain(): GamePreview = GamePreview(
    id = game.id,
    name = game.name,
    released = game.released,
    backgroundImage = game.backgroundImage,
    rating = game.rating,
    platforms = platforms.map { it.toDomain() },
    platformsNames = platforms.joinToString(separator = ", ") { it.name },
    genres = genres.joinToString(separator = ", ") { it.name },
    trailerUrl = null,
    trailerPreview = null
)

/** Маппер [PlatformEntity] -> [PlatformIcon] */
fun PlatformEntity.toDomain(): PlatformIcon = PlatformIcon(
    id = id,
    name = name,
    image = image
)

/** @return epoch-секунды из [value] (ISO-8601), если строка null или не парсится возвращает 0 */
private fun parseIsoToEpochSeconds(value: String?): Long {
    if (value.isNullOrBlank()) return 0L
    return runCatching { Instant.parse(value).epochSecond }.getOrDefault(0L)
}