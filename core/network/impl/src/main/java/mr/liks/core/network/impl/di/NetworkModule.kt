package mr.liks.core.network.impl.di

import io.ktor.client.engine.okhttp.OkHttp
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.impl.BuildConfig
import mr.liks.core.network.impl.client.HttpClientFactory
import mr.liks.core.network.impl.RawgApiImpl
import mr.liks.core.network.impl.client.RawgHttpClientConfig
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    single {
        OkHttp.create {
            config {
                connectTimeout(15, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
            }
        }
    }

    single {
        val config = RawgHttpClientConfig(
            baseUrl = BuildConfig.BASE_URL,
            apiKeyName = "key",
            apiKey = BuildConfig.RAWG_API_KEY,
            isDebug = BuildConfig.DEBUG
        )

        HttpClientFactory(engine = get(), config = config).create()
    }

    single<RawgApi> { RawgApiImpl(client = get()) }
}