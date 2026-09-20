package mr.liks.core.media.di

import android.annotation.SuppressLint
import mr.liks.core.common.CacheManager
import mr.liks.core.media.CacheManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@SuppressLint("UnsafeOptInUsageError")
val MediaModule = module {
    single<CacheManager> {
        CacheManagerImpl(context = androidContext())
    }
}