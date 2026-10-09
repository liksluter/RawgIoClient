package mr.liks.feature.details.impl.di

import mr.liks.core.common.StringProvider
import mr.liks.feature.details.impl.data.DetailsRepositoryImpl
import mr.liks.feature.details.impl.domain.repository.DetailsRepository
import mr.liks.feature.details.impl.domain.usecase.GetGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.GetGameMediaUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameMediaUseCase
import mr.liks.feature.details.impl.presentation.GameDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val DetailsModule = module {
    single<DetailsRepository> {
        DetailsRepositoryImpl(
            api = get(),
            database = get(),
            dispatchers = get()
        )
    }

    single { StringProvider(androidContext()) }

    factory { GetGameDetailsUseCase(get()) }
    factory { GetGameMediaUseCase(get()) }
    factory { RefreshGameDetailsUseCase(get()) }
    factory { RefreshGameMediaUseCase(get()) }

    viewModelOf(::GameDetailsViewModel)
}