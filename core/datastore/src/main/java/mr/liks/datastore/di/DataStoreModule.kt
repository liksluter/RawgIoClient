package mr.liks.datastore.di

import mr.liks.datastore.SettingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataStoreModule = module {
    single {
        SettingsDataStore(context = androidContext())
    }
}