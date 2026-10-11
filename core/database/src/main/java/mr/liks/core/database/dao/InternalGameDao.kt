package mr.liks.core.database.dao

import androidx.room.Dao
import androidx.room.Query

/** Dao для использования только внутри модуля */
@Dao
interface InternalGameDao {
    /**
     * Обновляет запись в `games` на основе параметров
     *
     * @param id id игры
     * @param slug слаг
     * @param name название
     * @param released дата релиза
     * @param backgroundImage лого
     * @param rating рейтинг
     * @param metacritic рейтинг на метакритике
     * @param playtime игровое время
     * @param updatedAt время обновления записи
     */
    @Query(
        """
    UPDATE games SET
        slug = :slug,
        name = :name,
        released = :released,
        backgroundImage = :backgroundImage,
        rating = :rating,
        metacritic = :metacritic,
        playtime = :playtime,
        updatedAt = :updatedAt
    WHERE id = :id
    """
    )
    suspend fun updateGameFromDetails(
        id: Long,
        slug: String,
        name: String,
        released: String?,
        backgroundImage: String?,
        rating: Double,
        metacritic: Int?,
        playtime: Int?,
        updatedAt: Long
    )
}