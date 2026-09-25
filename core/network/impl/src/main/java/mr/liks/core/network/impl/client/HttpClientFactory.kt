package mr.liks.core.network.impl.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/**
 * Фабрика http клиента для сервиса
 *
 * @property engine движок клиента
 * @property config конфиг клиента для сервиса
 */
@OptIn(ExperimentalSerializationApi::class)
class HttpClientFactory(
    private val engine: HttpClientEngine,
    private val config: RawgHttpClientConfig
) {
    /** @return клиент [HttpClient] */
    fun create(): HttpClient = HttpClient(engine) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(
                Json {
                    namingStrategy = JsonNamingStrategy.SnakeCase
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                    explicitNulls = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = config.requestTimeout
            connectTimeoutMillis = config.connectTimeout
            socketTimeoutMillis = config.socketTimeout
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = if (config.isDebug) LogLevel.BODY else LogLevel.NONE
        }

        HttpResponseValidator {
            convertErrorsToNetworkException()
        }

        defaultRequest {
            url(config.baseUrl)
            url.parameters.append(config.apiKeyName, config.apiKey)
            contentType(ContentType.Application.Json)
        }
    }
}