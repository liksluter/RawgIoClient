package mr.liks.core.database.dao

import kotlinx.coroutines.runBlocking
import mr.liks.core.database.entity.RemoteKeyEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/** Тесты для [RemoteKeyDao] */
class RemoteKeyDaoTest : DatabaseTest() {
    private val remoteKeyDao get() = db.remoteKeyDao()

    @Test
    fun `insert and retrieve remote key`() = runBlocking {
        val key = RemoteKeyEntity(gameId = 1, prevPage = null, nextPage = 2)
        remoteKeyDao.insert(key)
        val loaded = remoteKeyDao.remoteKeyById(1)
        assertEquals(key, loaded)
    }

    @Test
    fun `insertAll inserts multiple keys`() = runBlocking {
        val keys = listOf(
            RemoteKeyEntity(1, null, 2),
            RemoteKeyEntity(2, 1, 3)
        )
        remoteKeyDao.insertAll(keys)
        assertEquals(keys[0], remoteKeyDao.remoteKeyById(1))
        assertEquals(keys[1], remoteKeyDao.remoteKeyById(2))
    }

    @Test
    fun `clearAll removes all keys`() = runBlocking {
        remoteKeyDao.insert(RemoteKeyEntity(1, null, 2))
        remoteKeyDao.clearAll()
        assertNull(remoteKeyDao.remoteKeyById(1))
    }
}