package mr.liks.feature.feed.impl.data

import androidx.paging.PagingSource
import androidx.room.withTransaction
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.RemoteKeyDao
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.RawgApi
import mr.liks.feature.feed.impl.domain.repository.FeedRepository
import org.junit.After
import org.junit.Before
import org.junit.Test

class FeedRepositoryImplTest {
    private val api: RawgApi = mockk()
    private val database: RawgDatabase = mockk(relaxed = true)
    private val gameDao: GameDao = mockk(relaxed = true)
    private val remoteKeyDao: RemoteKeyDao = mockk(relaxed = true)
    private val dispatchers: DispatchersProvider = mockk {
        every { io } returns kotlinx.coroutines.Dispatchers.Unconfined
    }

    private lateinit var repository: FeedRepository

    @Before
    fun setup() {
        every { database.gameDao() } returns gameDao
        every { database.remoteKeyDao() } returns remoteKeyDao

        mockkStatic("androidx.room.RoomDatabaseKt")

        coEvery {
            database.withTransaction(captureCoroutine<suspend () -> Unit>())
        } coAnswers {
            coroutine<suspend () -> Unit>().captured.invoke()
        }

        repository = FeedRepositoryImpl(api, database, dispatchers)
    }

    @After
    fun tearDown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `refresh clears all tables in transaction`() = runTest {
        repository.refresh()

        coVerify { remoteKeyDao.clearAll() }
        coVerify { gameDao.clearPlatformCrossRefs() }
        coVerify { gameDao.clearGames() }
        coVerify { gameDao.clearPlatforms() }
    }

    @Test
    fun `getPagingData returns flow`() = runTest {
        val pagingSource = mockk<PagingSource<Int, GameWithPropertiesRelation>>()
        every { gameDao.pagingSource() } returns pagingSource

        val flow = repository.getPagingData()
        assert(flow != null)
    }
}