package mr.liks.feature.search.impl.di

import mr.liks.core.common.StringProvider
import mr.liks.feature.search.impl.data.SearchRepositoryImpl
import mr.liks.feature.search.impl.domain.repository.SearchRepository
import mr.liks.feature.search.impl.domain.usecase.DeleteSearchHistoryItemUseCase
import mr.liks.feature.search.impl.domain.usecase.GetSearchHistoryUseCase
import mr.liks.feature.search.impl.domain.usecase.SaveSearchQueryUseCase
import mr.liks.feature.search.impl.domain.usecase.SearchGamesUseCase
import mr.liks.feature.search.impl.presentation.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val SearchModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(
            api = get(),
            database = get(),
            dispatchers = get()
        )
    }

    single { StringProvider(androidContext()) }

    factory { SearchGamesUseCase(get()) }
    factory { GetSearchHistoryUseCase(get()) }
    factory { SaveSearchQueryUseCase(get()) }
    factory { DeleteSearchHistoryItemUseCase(get()) }

    viewModelOf(::SearchViewModel)
}