package mr.liks.feature.feed.impl.di

import mr.liks.feature.feed.impl.data.FeedRepositoryImpl
import mr.liks.feature.feed.impl.presentation.FeedViewModel
import mr.liks.feature.feed.impl.domain.repository.FeedRepository
import mr.liks.feature.feed.impl.domain.usecase.GetFeedPagingDataUseCase
import mr.liks.feature.feed.impl.domain.usecase.LoadNextPageUseCase
import mr.liks.feature.feed.impl.domain.usecase.RefreshFeedUseCase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val FeedModule = module {

    single<FeedRepository> {
        FeedRepositoryImpl(
            api = get(),
            database = get(),
            dispatchers = get()
        )
    }

    factory { GetFeedPagingDataUseCase(get()) }
    factory { RefreshFeedUseCase(get()) }
    factory { LoadNextPageUseCase(get()) }

    viewModelOf(::FeedViewModel)
}