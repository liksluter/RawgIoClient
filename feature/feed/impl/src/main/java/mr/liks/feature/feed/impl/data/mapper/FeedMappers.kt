package mr.liks.feature.feed.impl.data.mapper

import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.model.GamePreview
import mr.liks.core.model.PlatformIcon
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.PlatformDto

/** Маппер [GameListDto] (сеть) -> [GameEntity] (БД) */
fun GameListDto.toEntity(feedOrder: Long): GameEntity = GameEntity(
    id = id,
    slug = slug,
    name = name,
    released = released,
    backgroundImage = backgroundImage,
    rating = rating,
    ratingsCount = ratingsCount,
    metacritic = metacritic,
    feedOrder = feedOrder,
    playtime = playtime
)

/** Маппер [PlatformDto] (сеть) -> [PlatformEntity] (БД) */
fun PlatformDto.toEntity(): PlatformEntity = PlatformEntity(
    id = id,
    name = name,
    slug = slug,
    imageBackground = imageBackground
)

/**
 * @return Собирает [GamePlatformCrossRef] между игрой [gameId] и платформой [platformId],
 * [releasedAt] приходит в [mr.liks.core.network.api.dto.PlatformWrapperDto]
 */
fun platformCrossRef(gameId: Long, platformId: Long, releasedAt: String?): GamePlatformCrossRef =
    GamePlatformCrossRef(
        gameId = gameId,
        platformId = platformId,
        releasedAt = releasedAt
    )

/** Маппер связи [GameWithPropertiesRelation] в domain модель [GamePreview] */
fun GameWithPropertiesRelation.toDomain(): GamePreview = GamePreview(
    id = game.id,
    name = game.name,
    backgroundImage = game.backgroundImage,
    rating = game.rating,
    platforms = platforms.map { it.toDomain() },
    platformsNames = platforms.joinToString(separator = ", ") { it.name },
    feedOrder = game.feedOrder,
    // todo Трейлер для ленты подтягивается отдельно (см. FeedRepositoryImpl),
    // т.к. его может не быть вовсе, и запрашивать его для каждой карточки
    // до первого показа — лишний трафик.
    trailerUrl = null,
    trailerPreview = null,
)

/** Маппер [PlatformEntity] в domain модель [PlatformIcon] */
fun PlatformEntity.toDomain(): PlatformIcon = PlatformIcon(
    id = id,
    name = name,
    iconUrl = imageBackground
)