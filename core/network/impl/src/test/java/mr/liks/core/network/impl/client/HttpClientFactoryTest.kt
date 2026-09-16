package mr.liks.core.network.impl.client

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Тесты для [HttpClientFactory] */
class HttpClientFactoryTest {
    @Suppress("JUnitMalformedDeclaration")
    @Test
    fun `client is configured with base url and api key`() = runBlocking {
        val engine = MockEngine { request ->
            assertEquals("/api/games", request.url.encodedPath)
            assertEquals("test-api-key", request.url.parameters["key"])
            respond("{}", HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }

        val config = RawgHttpClientConfig(
            baseUrl = "https://api.rawg.io/api",
            apiKeyName = "key",
            apiKey = "test-api-key",
            isDebug = false
        )

        val client = HttpClientFactory(engine, config).create()
        client.get("games")
    }
}