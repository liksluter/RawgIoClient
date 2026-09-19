package mr.liks.core.database.di

import androidx.room.Room
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.GameDetailsDao
import mr.liks.core.database.dao.RemoteKeyDao
import mr.liks.core.database.dao.SearchHistoryDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseModule = module {
    single<RawgDatabase> {
        Room.databaseBuilder(
            androidContext(),
            RawgDatabase::class.java,
            RawgDatabase.NAME
        )
            .fallbackToDestructiveMigration(dropAllTables = true) // todo только для этапа начальной разработки
            .build()
    }

    single<GameDao> { get<RawgDatabase>().gameDao() }
    single<RemoteKeyDao> { get<RawgDatabase>().remoteKeyDao() }
    single<SearchHistoryDao> { get<RawgDatabase>().searchHistoryDao() }
    single<GameDetailsDao> { get<RawgDatabase>().gameDetailsDao() }
}