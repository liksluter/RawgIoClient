package mr.liks.feature.feed.impl.data

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
import mr.liks.core.database.dao.RemoteKeyDao
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.GamesListResponse
import mr.liks.core.network.api.dto.PlatformDto
import mr.liks.core.network.api.dto.PlatformWrapperDto
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class FeedRemoteMediatorTest {

    private val api: RawgApi = mockk()
    private val database: RawgDatabase = mockk(relaxed = true)
    private val gameDao: GameDao = mockk(relaxed = true)
    private val remoteKeyDao: RemoteKeyDao = mockk(relaxed = true)

    private lateinit var mediator: FeedRemoteMediator

    @Before
    fun setup() {
        every { database.gameDao() } returns gameDao
        every { database.remoteKeyDao() } returns remoteKeyDao

        mockkStatic("androidx.room.RoomDatabaseKt")
        coEvery {
            database.withTransaction<Unit>(any())
        } coAnswers {
            @Suppress("UNCHECKED_CAST")
            val block = args.first { it is Function1<*, *> } as suspend () -> Unit
            block.invoke()
        }

        mediator = FeedRemoteMediator(api, database, pageSize = PAGE_SIZE)
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `initialize returns LAUNCH_INITIAL_REFRESH when no last insert`() = runTest {
        coEvery { remoteKeyDao.maxInsertedAt() } returns null
        val action = mediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, action)
    }

    @Test
    fun `initialize returns SKIP_INITIAL_REFRESH when recent insert`() = runTest {
        coEvery { remoteKeyDao.maxInsertedAt() } returns System.currentTimeMillis()
        val action = mediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, action)
    }

    @Test
    fun `load REFRESH fetches first page and saves data`() = runTest {
        // given
        coEvery {
            api.getGames(page = 1, pageSize = PAGE_SIZE, ordering = "-added")
        } returns GamesListResponse(
            count = 1,
            next = null,
            previous = null,
            results = listOf(sampleGameDto())
        )

        val result = mediator.load(LoadType.REFRESH, emptyState())

        val success = when (result) {
            is RemoteMediator.MediatorResult.Success -> result
            is RemoteMediator.MediatorResult.Error ->
                throw AssertionError("Mediator returned Error", result.throwable)
        }
        assertTrue(
            "endOfPaginationReached should be true because next == null",
            success.endOfPaginationReached
        )

        coVerify { remoteKeyDao.clearAll() }
        coVerify { gameDao.clearGames() }
        coVerify { gameDao.clearPlatforms() }
        coVerify { gameDao.upsertGames(any()) }
        coVerify { gameDao.upsertPlatforms(any()) }
        coVerify { gameDao.insertPlatformCrossRefs(any()) }
        coVerify { remoteKeyDao.insertAll(any()) }
    }

    @Test
    fun `load REFRESH marks endOfPaginationReached false when next is not null`() = runTest {
        coEvery {
            api.getGames(page = 1, pageSize = PAGE_SIZE, ordering = "-added")
        } returns GamesListResponse(
            count = 1,
            next = "https://api.rawg.io/api/games?page=2",
            previous = null,
            results = listOf(sampleGameDto())
        )

        val result = mediator.load(LoadType.REFRESH, emptyState())

        val success = when (result) {
            is RemoteMediator.MediatorResult.Success -> result
            is RemoteMediator.MediatorResult.Error ->
                throw AssertionError("Mediator returned Error", result.throwable)
        }
        assertEquals(false, success.endOfPaginationReached)
    }

    @Test
    fun `load APPEND returns end when no remote key and db has no data`() = runTest {
        coEvery { remoteKeyDao.remoteKeyById(any()) } returns null
        coEvery { gameDao.lastFeedGameId() } returns null
        coEvery { gameDao.count() } returns 0

        val result = mediator.load(LoadType.APPEND, emptyState())

        val success = result as? RemoteMediator.MediatorResult.Success
            ?: error("Expected Success but was $result")
        assertTrue(success.endOfPaginationReached)
    }

    @Test
    fun `load APPEND returns not end when no remote key but db has data`() = runTest {
        coEvery { remoteKeyDao.remoteKeyById(any()) } returns null
        coEvery { gameDao.lastFeedGameId() } returns null
        coEvery { gameDao.count() } returns 5

        val result = mediator.load(LoadType.APPEND, emptyState())

        val success = result as? RemoteMediator.MediatorResult.Success
            ?: error("Expected Success but was $result")
        assertEquals(false, success.endOfPaginationReached)
    }

    @Test
    fun `load APPEND returns end when remote key has no nextPage`() = runTest {
        coEvery { remoteKeyDao.remoteKeyById(any()) } returns
                mr.liks.core.database.entity.RemoteKeyEntity(
                    gameId = 1L, prevPage = null, nextPage = null
                )
        coEvery { gameDao.lastFeedGameId() } returns 1L

        val result = mediator.load(LoadType.APPEND, emptyState())

        val success = result as? RemoteMediator.MediatorResult.Success
            ?: error("Expected Success but was $result")
        assertTrue(success.endOfPaginationReached)
    }

    @Test
    fun `load PREPEND always returns end`() = runTest {
        val result = mediator.load(LoadType.PREPEND, emptyState())

        val success = result as? RemoteMediator.MediatorResult.Success
            ?: error("Expected Success but was $result")
        assertTrue(success.endOfPaginationReached)
    }

    @Test
    fun `load returns Error when api throws`() = runTest {
        coEvery { api.getGames(any(), any(), any()) } throws RuntimeException("Network")

        val result = mediator.load(LoadType.REFRESH, emptyState())

        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    private fun emptyState() = PagingState<Int, GameWithPropertiesRelation>(
        pages = emptyList(),
        anchorPosition = null,
        config = mockk(relaxed = true),
        leadingPlaceholderCount = 0
    )

    private fun sampleGameDto(): GameListDto = GameListDto(
        id = 1L,
        slug = "s",
        name = "N",
        released = "2023",
        backgroundImage = "bg",
        rating = 4.0,
        ratingsCount = 10,
        metacritic = 80,
        playtime = 5,
        platforms = listOf(
            PlatformWrapperDto(
                platform = PlatformDto(1L, "PC", "pc", "icon"),
                releasedAt = "2023-01-01"
            )
        )
    )

    private companion object {
        const val PAGE_SIZE = 20
    }
}