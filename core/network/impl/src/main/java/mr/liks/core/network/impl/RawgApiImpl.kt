package mr.liks.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.dto.GameDetailsDto
import mr.liks.core.network.api.dto.GamesListResponse
import mr.liks.core.network.api.dto.MoviesResponse
import mr.liks.core.network.api.dto.ScreenshotsResponse

/** Реализация [RawgApi] */
class RawgApiImpl(
    private val client: HttpClient
): RawgApi {
    override suspend fun getGames(
        page: Int,
        pageSize: Int,
        ordering: String
    ): GamesListResponse = client.get("games") {
        parameter("page", page)
        parameter("page_size", pageSize)
        parameter("ordering", ordering)
    }.body()

    override suspend fun searchGames(
        query: String,
        page: Int,
        pageSize: Int
    ): GamesListResponse = client.get("games") {
        parameter("search", query)
        parameter("page", page)
        parameter("page_size", pageSize)
    }.body()

    override suspend fun getGameDetails(gameId: Long): GameDetailsDto =
        client.get("games/$gameId").body()

    override suspend fun getGameTrailers(gameId: Long): MoviesResponse =
        client.get("games/$gameId/movies").body()

    override suspend fun getGameScreenshots(gameId: Long): ScreenshotsResponse =
        client.get("games/$gameId/screenshots").body()
}