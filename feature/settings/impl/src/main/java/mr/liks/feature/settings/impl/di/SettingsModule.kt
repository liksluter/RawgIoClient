package mr.liks.feature.settings.impl.di

import mr.liks.core.common.StringProvider
import mr.liks.feature.settings.impl.data.SettingsRepositoryImpl
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository
import mr.liks.feature.settings.impl.domain.usecase.ClearCacheUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetAppSettingsUseCase
import mr.liks.feature.settings.impl.domain.usecase.GetCacheSizeUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetDynamicColorUseCase
import mr.liks.feature.settings.impl.domain.usecase.SetThemeUseCase
import mr.liks.feature.settings.impl.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val SettingsModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            dataStore = get(),
            cacheManager = get(),
            dispatchers = get(),
            externalScope = get()
        )
    }

    single { StringProvider(androidContext()) }

    factory { GetAppSettingsUseCase(get()) }
    factory { SetThemeUseCase(get()) }
    factory { SetDynamicColorUseCase(get()) }
    factory { GetCacheSizeUseCase(get()) }
    factory { ClearCacheUseCase(get()) }

    viewModel {
        SettingsViewModel(
            getAppSettings = get(),
            setTheme = get(),
            setDynamicColor = get(),
            getCacheSize = get(),
            clearCache = get(),
            stringProvider = get(),
            logger = get()
        )
    }
}