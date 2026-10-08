package mr.liks.feature.search.impl.data

import androidx.room.withTransaction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.SearchHistoryDao
import mr.liks.core.database.entity.SearchHistoryEntity
import mr.liks.core.network.api.RawgApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {
    private val api = mockk<RawgApi>(relaxed = true)
    private val database = mockk<RawgDatabase>()
    private val gameDao = mockk<GameDao>(relaxed = true)
    private val searchHistoryDao = mockk<SearchHistoryDao>(relaxed = true)
    private val dispatchers = mockk<DispatchersProvider> {
        every { io } returns UnconfinedTestDispatcher()
    }

    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setup() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        coEvery {
            database.withTransaction(captureCoroutine<suspend () -> Unit>())
        } coAnswers {
            coroutine<suspend () -> Unit>().captured.invoke()
        }
        every { database.gameDao() } returns gameDao
        every { database.searchHistoryDao() } returns searchHistoryDao

        repository = SearchRepositoryImpl(api, database, dispatchers)
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `observeHistory maps entities`() = runTest {
        val entity = SearchHistoryEntity(id = 1L, query = "query", searchedAt = 123L)
        every { searchHistoryDao.observeHistory(60) } returns flowOf(listOf(entity))

        val result = repository.observeHistory().first()

        assertEquals(1, result.size)
        assertEquals("query", result[0].query)
    }

    @Test
    fun `deleteQuery deletes history and search entries`() = runTest {
        repository.deleteQuery("query")

        coVerify { searchHistoryDao.deleteByQuery("query") }
        coVerify { gameDao.clearSearchEntries("query") }
    }

    @Test
    fun `clearHistory clears all`() = runTest {
        repository.clearHistory()

        coVerify { searchHistoryDao.clearAll() }
        coVerify { gameDao.clearSearchEntriesExcept(emptyList()) }
    }
}