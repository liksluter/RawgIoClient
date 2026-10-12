package mr.liks.rawgioclient.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import mr.liks.core.common.DefaultDispatchersProvider
import mr.liks.core.common.DispatchersProvider
import mr.liks.feature.settings.impl.di.VERSION_NAME_QUALIFIER_NAME
import mr.liks.rawgioclient.BuildConfig
import mr.liks.rawgioclient.presentation.ThemeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppModule = module {
    single<DispatchersProvider> { DefaultDispatchersProvider() }

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single(named(VERSION_NAME_QUALIFIER_NAME)) { BuildConfig.VERSION_NAME }

    viewModelOf(::ThemeViewModel)
}