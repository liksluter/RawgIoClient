package mr.liks.rawgioclient.di

import mr.liks.core.common.DefaultDispatchersProvider
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.common.applocagger.TimberAppLogger
import org.koin.dsl.module

val CommonModule = module {
    single<DispatchersProvider> { DefaultDispatchersProvider() }
    single<AppLogger> { TimberAppLogger() }
}