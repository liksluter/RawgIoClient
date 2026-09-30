package mr.liks.core.database.dao

import kotlinx.coroutines.runBlocking
import mr.liks.core.database.entity.RemoteKeyEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

/** Тесты для [RemoteKeyDao] */
class RemoteKeyDaoTest : DatabaseTest() {
    private val remoteKeyDao get() = db.remoteKeyDao()

    @Test
    fun `insertKey and lastRemoteKey`() = runBlocking {
        val key = RemoteKeyEntity(
            ordering = "popular",
            prevPage = null,
            nextPage = 2,
            insertedAt = 1_000
        )

        remoteKeyDao.insertKey(key)

        assertEquals(key, remoteKeyDao.lastRemoteKey("popular"))
    }

    @Test
    fun `insertKey replaces existing key for same ordering`() = runBlocking {
        remoteKeyDao.insertKey(RemoteKeyEntity("popular", null, 2, 1_000))

        val updated = RemoteKeyEntity("popular", 1, 3, 2_000)
        remoteKeyDao.insertKey(updated)

        assertEquals(updated, remoteKeyDao.lastRemoteKey("popular"))
    }

    @Test
    fun `clearForOrdering removes only matching ordering`() = runBlocking {
        remoteKeyDao.insertKey(RemoteKeyEntity("popular", null, 2, 1_000))
        remoteKeyDao.insertKey(RemoteKeyEntity("fresh", null, 5, 2_000))

        remoteKeyDao.clearForOrdering("popular")

        assertNull(remoteKeyDao.lastRemoteKey("popular"))
        assertNotNull(remoteKeyDao.lastRemoteKey("fresh"))
    }

    @Test
    fun `insertedAt returns max for ordering`() = runBlocking {
        remoteKeyDao.insertKey(RemoteKeyEntity("popular", null, 1, 1_000))
        remoteKeyDao.insertKey(RemoteKeyEntity("popular", 1, 2, 3_000))
        remoteKeyDao.insertKey(RemoteKeyEntity("fresh", null, 1, 5_000))

        assertEquals(3_000L, remoteKeyDao.insertedAt("popular"))
        assertNull(remoteKeyDao.insertedAt("missing"))
    }
}