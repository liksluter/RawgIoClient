package mr.liks.core.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity

/**
 * Свзяь детальная информация об игре и медиа-файлы
 *
 * @property details детальная информация об игре
 * @property trailers трейлеры
 * @property screenshots скриншоты
 */
data class GameDetailsWithMediaRelation(
    @Embedded val details: GameDetailsEntity,

    @Relation(
        parentColumn = "gameId",
        entityColumn = "gameId"
    )
    val trailers: List<TrailerEntity>,

    @Relation(
        parentColumn = "gameId",
        entityColumn = "gameId"
    )
    val screenshots: List<ScreenshotEntity>
)