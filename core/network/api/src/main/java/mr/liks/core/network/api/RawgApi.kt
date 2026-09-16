package mr.liks.core.network.api

import mr.liks.core.network.api.dto.GameDetailsDto
import mr.liks.core.network.api.dto.GamesListResponse
import mr.liks.core.network.api.dto.MoviesResponse
import mr.liks.core.network.api.dto.ScreenshotsResponse

/** Контракт rawg.io api для Ktor */
interface RawgApi {
    /**
     * Получить список игр
     *
     * @param page номер страницы
     * @param pageSize кол-во результатов на страницу
     * @param ordering порядок сортировки
     * @return ответ со списком игр [GamesListResponse]
     */
    suspend fun getGames(
        page: Int,
        pageSize: Int = 20,
        ordering: String = "-added"
    ): GamesListResponse

    /**
     * Найти игры
     *
     * @param query поисковый запрос
     * @param page номер страницы
     * @param pageSize кол-во результатов на страницу
     * @return ответ со списком игр [GamesListResponse]
     */
    suspend fun searchGames(
        query: String,
        page: Int,
        pageSize: Int = 20
    ): GamesListResponse

    /** @return подробности об игре [GameDetailsDto] по ее id [gameId] */
    suspend fun getGameDetails(
        gameId: Long,
    ): GameDetailsDto

    /** @return список трейлеров [MoviesResponse] по id игры [gameId] */
    suspend fun getGameTrailers(
        gameId: Long,
    ): MoviesResponse

    /** @return список игровых скриншотов [ScreenshotsResponse] по id игры [gameId] */
    suspend fun getGameScreenshots(
        gameId: Long,
    ): ScreenshotsResponse
}