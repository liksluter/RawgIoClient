package mr.liks.core.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity

/**
 * Свзяь информация об игре и ее свойства
 *
 * @property game информацуия об игре
 * @property platforms платформы
 * @property genres жанры
 */
data class GameWithPropertiesRelation(
    @Embedded val game: GameEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = GamePlatformCrossRef::class,
            parentColumn = "gameId",
            entityColumn = "platformId"
        )
    )
    val platforms: List<PlatformEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = GameGenreCrossRef::class,
            parentColumn = "gameId",
            entityColumn = "genreId"
        )
    )
    val genres: List<GenreEntity>
)
