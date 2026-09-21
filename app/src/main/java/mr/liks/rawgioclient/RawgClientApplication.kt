package mr.liks.rawgioclient

import android.app.Application
import mr.liks.core.database.di.DatabaseModule
import mr.liks.core.media.di.MediaModule
import mr.liks.core.network.impl.di.NetworkModule
import mr.liks.rawgioclient.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RawgClientApplication: Application() {
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
                MediaModule,
            )
        }
    }
}