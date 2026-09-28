package mr.liks.rawgioclient

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import mr.liks.core.database.di.DatabaseModule
import mr.liks.core.media.di.MediaModule
import mr.liks.core.network.impl.di.NetworkModule
import mr.liks.core.ui.image.RawgImageLoader
import mr.liks.datastore.di.DataStoreModule
import mr.liks.feature.details.impl.di.DetailsModule
import mr.liks.feature.feed.impl.di.FeedModule
import mr.liks.feature.settings.impl.di.SettingsModule
import mr.liks.rawgioclient.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RawgClientApplication: Application(), SingletonImageLoader.Factory {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@RawgClientApplication)
            modules(
                AppModule,
                NetworkModule,
                DatabaseModule,
                DataStoreModule,
                MediaModule,
                FeedModule,
                DetailsModule,
                SettingsModule,
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        RawgImageLoader.create(context)
}