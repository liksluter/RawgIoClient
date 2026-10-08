package mr.liks.feature.search.impl.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.SearchHistoryDao
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.GamesListResponse
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediatorTest {
    private val api = mockk<RawgApi>()
    private val database = mockk<RawgDatabase>()
    private val gameDao = mockk<GameDao>(relaxed = true)
    private val searchHistoryDao = mockk<SearchHistoryDao>(relaxed = true)
    private val pageSize = 20

    @Before
    fun setup() {
        every { database.gameDao() } returns gameDao
        every { database.searchHistoryDao() } returns searchHistoryDao

        mockkStatic("androidx.room.RoomDatabaseKt")
        
        coEvery {
            database.withTransaction<Unit>(any())
        } coAnswers {
            @Suppress("UNCHECKED_CAST")
            val block = args.first { it is Function1<*, *> } as suspend () -> Unit
            block.invoke()
        }
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `load with blank query returns success end`() = runTest {
        val mediator = SearchRemoteMediator("", api, database, pageSize)

        val result = mediator.load(LoadType.REFRESH, state())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.searchGames(any(), any(), any()) }
    }

    @Test
    fun `load with non refresh returns success end`() = runTest {
        val mediator = SearchRemoteMediator("query", api, database, pageSize)

        val result = mediator.load(LoadType.APPEND, state())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { api.searchGames(any(), any(), any()) }
    }

    @Test
    fun `load success inserts data`() = runTest {
        val dto = mockk<GameListDto> {
            every { id } returns 1L
            every { slug } returns "slug"
            every { name } returns "Name"
            every { released } returns null
            every { backgroundImage } returns null
            every { rating } returns 0.0
            every { ratingsCount } returns 0
            every { metacritic } returns 0
            every { playtime } returns 0
            every { platforms } returns emptyList()
            every { genres } returns emptyList()
        }
        val response = mockk<GamesListResponse> {
            every { results } returns listOf(dto)
        }
        coEvery { api.searchGames("query", 1, pageSize) } returns response

        val mediator = SearchRemoteMediator("query", api, database, pageSize)

        val result = mediator.load(LoadType.REFRESH, state())

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify { gameDao.upsertGames(match { it.size == 1 && it[0].id == 1L }) }
        coVerify { gameDao.clearSearchEntries("query") }
        coVerify {
            gameDao.upsertSearchEntries(
                match {
                    it.size == 1 &&
                            it[0].query == "query" &&
                            it[0].gameId == 1L &&
                            it[0].position == 0
                }
            )
        }
    }

    @Test
    fun `load error returns Error`() = runTest {
        coEvery { api.searchGames(any(), any(), any()) } throws RuntimeException("network")

        val mediator = SearchRemoteMediator("query", api, database, pageSize)

        val result = mediator.load(LoadType.REFRESH, state())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    private fun state(): PagingState<Int, GameWithPropertiesRelation> =
        mockk(relaxed = true)
}