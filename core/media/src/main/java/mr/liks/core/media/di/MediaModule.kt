package mr.liks.core.media.di

import android.annotation.SuppressLint
import mr.liks.core.common.CacheManager
import mr.liks.core.media.CacheManagerImpl
import mr.liks.core.media.TrailerPlayerController
import mr.liks.core.media.TrailerPlayerControllerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val MediaModule = module {
    single<CacheManager> {
        CacheManagerImpl(context = androidContext())
    }

    factory<TrailerPlayerController> { TrailerPlayerControllerImpl(androidContext()) }
}