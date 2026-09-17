package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import mr.liks.core.database.relation.GameDetailsWithMediaRelation
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity

/** DAO для деталей игры, трейлеров и скриншотов */
@Dao
interface GameDetailsDao {
    /** @return [Flow] для наблюдения за деталями об одной игроы [GameDetailsEntity] с идентфиикатором [gameId] */
    @Query("SELECT * FROM game_details WHERE gameId = :gameId")
    fun observeDetails(gameId: Long): Flow<GameDetailsEntity?>

    /** @return [Flow] для наблюдения за связью [GameDetailsWithMediaRelation] с идентфиикатором [gameId] */
    @Transaction
    @Query("SELECT * FROM game_details WHERE gameId = :gameId")
    fun observeDetailsWithMedia(gameId: Long): Flow<GameDetailsWithMediaRelation?>

    /** @return [Flow] для наблюдения за списком трейлеров [TrailerEntity] с идентфиикатором [gameId] */
    @Query("SELECT * FROM trailers WHERE gameId = :gameId ORDER BY id ASC")
    fun observeTrailers(gameId: Long): Flow<List<TrailerEntity>>

    /** @return [Flow] для наблюдения за списком скриншотов [ScreenshotEntity] с идентфиикатором [gameId] */
    @Query(
        """
        SELECT * FROM screenshots
        WHERE gameId = :gameId AND isDeleted = 0
        ORDER BY id ASC
        """
    )
    fun observeScreenshots(gameId: Long): Flow<List<ScreenshotEntity>>

    /** Вставляет/обновляет детали об игре из [details] */
    @Upsert
    suspend fun upsertDetails(details: GameDetailsEntity)

    /** Вставляет/обновляет список трейлеров из [trailers] */
    @Upsert
    suspend fun upsertTrailers(trailers: List<TrailerEntity>)

    /** Вставляет/обновляет список скриншотов из [screenshots] */
    @Upsert
    suspend fun upsertScreenshots(screenshots: List<ScreenshotEntity>)

    /** Удаляет трейлеры из `trailers` на основе [gameId] */
    @Query("DELETE FROM trailers WHERE gameId = :gameId")
    suspend fun clearTrailers(gameId: Long)

    /** Удаляет скриншоты из `screenshots` на основе [gameId] */
    @Query("DELETE FROM screenshots WHERE gameId = :gameId")
    suspend fun clearScreenshots(gameId: Long)

    /** Удаляет детали об игре из `game_details` на основе [gameId] */
    @Query("DELETE FROM game_details WHERE gameId = :gameId")
    suspend fun clearDetails(gameId: Long)
}