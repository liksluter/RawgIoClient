package mr.liks.core.network.impl.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import mr.liks.core.network.impl.exception.NetworkException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/** Тесты для логики обработчика [io.ktor.client.plugins.CallRequestExceptionHandler] */
class ErrorsToNetworkExceptionConverterTest {
    private fun createClient(engine: MockEngine): HttpClient =
        HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
            HttpResponseValidator {
                convertErrorsToNetworkException()
            }
        }

    @Test
    fun `throws ClientError on 4xx`() {
        val engine = MockEngine.Companion {
            respondError(
                HttpStatusCode.BadRequest,
                "Bad Request"
            )
        }
        val client = createClient(engine)

        val exception = Assertions.assertThrows(NetworkException.ClientError::class.java) {
            runBlocking { client.get("any") }
        }
        Assertions.assertEquals(400, exception.code)
    }

    @Test
    fun `throws ServerError on 5xx`() {
        val engine = MockEngine.Companion {
            respondError(
                HttpStatusCode.InternalServerError,
                "Server Error"
            )
        }
        val client = createClient(engine)

        val exception = Assertions.assertThrows(NetworkException.ServerError::class.java) {
            runBlocking { client.get("any") }
        }
        Assertions.assertEquals(500, exception.code)
    }

    @Test
    fun `throws Timeout on HttpRequestTimeoutException`() {
        val engine = MockEngine.Companion {
            throw HttpRequestTimeoutException(
                url = "https://example.com/any",
                timeoutMillis = 1000L
            )
        }
        val client = createClient(engine)

        Assertions.assertThrows(NetworkException.Timeout::class.java) {
            runBlocking { client.get("any") }
        }
    }

    @Test
    fun `throws Serialization on SerializationException`() {
        val engine = MockEngine.Companion {
            throw SerializationException("Serialization error")
        }
        val client = createClient(engine)

        Assertions.assertThrows(NetworkException.Serialization::class.java) {
            runBlocking { client.get("any") }
        }
    }

    @Test
    fun `throws NoConnection on IOException`() {
        val engine = MockEngine.Companion {
            throw kotlinx.io.IOException("No connection")
        }
        val client = createClient(engine)

        Assertions.assertThrows(NetworkException.NoConnection::class.java) {
            runBlocking { client.get("any") }
        }
    }

    @Test
    fun `throws Unknown on unexpected exception`() {
        val engine = MockEngine.Companion {
            throw RuntimeException("Unexpected")
        }
        val client = createClient(engine)

        Assertions.assertThrows(NetworkException.Unknown::class.java) {
            runBlocking { client.get("any") }
        }
    }
}