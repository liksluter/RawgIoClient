package mr.liks.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.GameDetailsDao
import mr.liks.core.database.dao.RemoteKeyDao
import mr.liks.core.database.dao.SearchHistoryDao
import mr.liks.core.database.entity.DeveloperEntity
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.GameDeveloperCrossRef
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GamePublisherCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.PublisherEntity
import mr.liks.core.database.entity.RemoteKeyEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.SearchHistoryEntity
import mr.liks.core.database.entity.TrailerEntity

/** База данных, кэширует данные с rawg.io */
@Database(
    entities = [
        GameEntity::class,
        PlatformEntity::class,
        GamePlatformCrossRef::class,
        GenreEntity::class,
        GameGenreCrossRef::class,
        DeveloperEntity::class,
        PublisherEntity::class,
        GameDeveloperCrossRef::class,
        GamePublisherCrossRef::class,
        GameDetailsEntity::class,
        TrailerEntity::class,
        ScreenshotEntity::class,
        RemoteKeyEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class RawgDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun gameDetailsDao(): GameDetailsDao

    companion object {
        const val NAME = "rawg.db"
    }
}