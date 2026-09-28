package mr.liks.feature.details.impl.data.mapper

import mr.liks.core.database.entity.DeveloperEntity
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.PublisherEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity
import mr.liks.core.model.Developer
import mr.liks.core.model.Genre
import mr.liks.core.model.PlatformIcon
import mr.liks.core.model.Publisher
import mr.liks.core.model.Screenshot
import mr.liks.core.model.Trailer
import mr.liks.core.network.api.dto.DeveloperDto
import mr.liks.core.network.api.dto.GameDetailsDto
import mr.liks.core.network.api.dto.GenreDto
import mr.liks.core.network.api.dto.MovieDto
import mr.liks.core.network.api.dto.PlatformDto
import mr.liks.core.network.api.dto.PublisherDto
import mr.liks.core.network.api.dto.ScreenshotDto

/** Маппер [GameDetailsDto] (сеть) -> [GameDetailsEntity] (БД) */
fun GameDetailsDto.toEntity(): GameDetailsEntity = GameDetailsEntity(
    gameId = id,
    description = description,
    descriptionRaw = descriptionRaw,
    website = website,
    redditUrl = redditUrl,
    metacriticUrl = metacriticUrl
)

/** Маппер [DeveloperDto] (сеть) -> [DeveloperEntity] (БД) */
fun DeveloperDto.toEntity(): DeveloperEntity = DeveloperEntity(
    id = id,
    name = name,
    slug = slug
)

/** Маппер [PublisherDto] (сеть) -> [PublisherEntity] (БД) */
fun PublisherDto.toEntity(): PublisherEntity = PublisherEntity(
    id = id,
    name = name,
    slug = slug
)

/** Маппер [GenreDto] (сеть) -> [GenreEntity] (БД) */
fun GenreDto.toEntity(): GenreEntity = GenreEntity(
    id = id,
    name = name,
    slug = slug
)

/** Маппер [PlatformDto] (сеть) -> [PlatformEntity] (БД) */
fun PlatformDto.toEntity(): PlatformEntity = PlatformEntity(
    id = id,
    name = name,
    slug = slug,
    imageBackground = imageBackground
)

/** Маппер [MovieDto] (сеть) -> [TrailerEntity] (БД) */
fun MovieDto.toEntity(gameId: Long): TrailerEntity = TrailerEntity(
    id = id,
    gameId = gameId,
    name = name,
    preview = preview,
    data480 = data?.data480,
    dataMax = data?.max
)

/** Маппер [ScreenshotDto] (сеть) -> [ScreenshotEntity] (БД) */
fun ScreenshotDto.toEntity(gameId: Long): ScreenshotEntity = ScreenshotEntity(
    id = id,
    gameId = gameId,
    image = image,
    width = width,
    height = height,
    isDeleted = isDeleted
)

/** Маппер [TrailerEntity] в domain модель [Trailer] */
fun TrailerEntity.toDomain(): Trailer = Trailer(
    id = id,
    name = name,
    preview = preview,
    data480 = data480,
    dataMax = dataMax
)

/** Маппер [ScreenshotEntity] в domain модель [Screenshot] */
fun ScreenshotEntity.toDomain(): Screenshot = Screenshot(
    id = id,
    image = image,
    width = width,
    height = height
)

/** Маппер [DeveloperEntity] в domain модель [Developer] */
fun DeveloperEntity.toDomain(): Developer = Developer(id = id, name = name)

/** Маппер [PublisherEntity] в domain модель [Publisher] */
fun PublisherEntity.toDomain(): Publisher = Publisher(id = id, name = name)

/** Маппер [GenreEntity] в domain модель [Genre] */
fun GenreEntity.toDomain(): Genre = Genre(id = id, name = name)

/** Маппер [PlatformEntity] в domain модель [PlatformIcon] */
fun PlatformEntity.toDomain(): PlatformIcon = PlatformIcon(
    id = id,
    name = name,
    iconUrl = imageBackground
)