package mr.liks.rawgioclient.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import mr.liks.core.common.DefaultDispatchersProvider
import mr.liks.core.common.DispatchersProvider
import mr.liks.rawgioclient.presentation.ThemeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val AppModule = module {
    single<DispatchersProvider> { DefaultDispatchersProvider() }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    viewModelOf(::ThemeViewModel)
}