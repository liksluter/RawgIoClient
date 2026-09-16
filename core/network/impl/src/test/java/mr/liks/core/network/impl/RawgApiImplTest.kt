package mr.liks.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import mr.liks.core.network.api.dto.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Тесты для [RawgApiImpl] */
class RawgApiImplTest {
    private val json = Json { ignoreUnknownKeys = true }

    private fun createClient(engine: MockEngine): HttpClient =
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

    @Test
    fun `getGames sends correct parameters and parses response`() = runTest {
        val expected = GamesListResponse(
            count = 1,
            results = listOf(
                GameListDto(1, "slug", "Name", "2023-01-01", "img", 4.5, 100, 90, 10, emptyList())
            )
        )
        val responseJson = json.encodeToString(GamesListResponse.serializer(), expected)

        val engine = MockEngine { request ->
            assertEquals("/games", request.url.encodedPath)
            assertEquals("1", request.url.parameters["page"])
            assertEquals("20", request.url.parameters["page_size"])
            assertEquals("-added", request.url.parameters["ordering"])
            respond(
                responseJson,
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RawgApiImpl(createClient(engine))
        val result = api.getGames(page = 1, pageSize = 20, ordering = "-added")
        assertEquals(expected, result)
    }

    @Test
    fun `searchGames sends correct parameters and parses response`() = runTest {
        val expected = GamesListResponse(count = 0, results = emptyList())
        val responseJson = json.encodeToString(GamesListResponse.serializer(), expected)

        val engine = MockEngine { request ->
            assertEquals("/games", request.url.encodedPath)
            assertEquals("query", request.url.parameters["search"])
            assertEquals("2", request.url.parameters["page"])
            assertEquals("10", request.url.parameters["page_size"])
            respond(
                responseJson,
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RawgApiImpl(createClient(engine))
        val result = api.searchGames(query = "query", page = 2, pageSize = 10)
        assertEquals(expected, result)
    }

    @Test
    fun `getGameDetails sends correct path and parses response`() = runTest {
        val expected = GameDetailsDto(
            id = 42,
            slug = "game",
            name = "Game"
        )
        val responseJson = json.encodeToString(GameDetailsDto.serializer(), expected)

        val engine = MockEngine { request ->
            assertEquals("/games/42", request.url.encodedPath)
            respond(
                responseJson,
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RawgApiImpl(createClient(engine))
        val result = api.getGameDetails(42L)
        assertEquals(expected, result)
    }

    @Test
    fun `getGameTrailers sends correct path and parses response`() = runTest {
        val expected = MoviesResponse(count = 0, results = emptyList())
        val responseJson = json.encodeToString(MoviesResponse.serializer(), expected)

        val engine = MockEngine { request ->
            assertEquals("/games/42/movies", request.url.encodedPath)
            respond(
                responseJson,
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RawgApiImpl(createClient(engine))
        val result = api.getGameTrailers(42L)
        assertEquals(expected, result)
    }

    @Test
    fun `getGameScreenshots sends correct path and parses response`() = runTest {
        val expected = ScreenshotsResponse(count = 0, results = emptyList())
        val responseJson = json.encodeToString(ScreenshotsResponse.serializer(), expected)

        val engine = MockEngine { request ->
            assertEquals("/games/42/screenshots", request.url.encodedPath)
            respond(
                responseJson,
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val api = RawgApiImpl(createClient(engine))
        val result = api.getGameScreenshots(42L)
        assertEquals(expected, result)
    }
}