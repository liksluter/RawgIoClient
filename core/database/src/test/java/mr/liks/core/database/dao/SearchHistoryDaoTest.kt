package mr.liks.core.database.dao

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mr.liks.core.database.entity.SearchHistoryEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** Тесты для [SearchHistoryDao] */
class SearchHistoryDaoTest : DatabaseTest() {
    private val searchHistoryDao get() = db.searchHistoryDao()

    @Test
    fun `upsert and observe history`() = runBlocking {
        val entry = SearchHistoryEntity(query = "test", searchedAt = 1000)
        searchHistoryDao.upsert(entry)
        val history = searchHistoryDao.observeHistory().first()
        assertEquals(1, history.size)
        assertEquals("test", history[0].query)
    }

    @Test
    fun `upsert updates existing query with new timestamp`() = runBlocking {
        searchHistoryDao.upsert(SearchHistoryEntity(query = "test", searchedAt = 1000))
        searchHistoryDao.upsert(SearchHistoryEntity(query = "test", searchedAt = 2000))
        val history = searchHistoryDao.observeHistory().first()
        assertEquals(1, history.size)
        assertEquals(2000, history[0].searchedAt)
    }

    @Test
    fun `deleteByQuery removes entry`() = runBlocking {
        searchHistoryDao.upsert(SearchHistoryEntity(query = "test", searchedAt = 1000))
        searchHistoryDao.deleteByQuery("test")
        val history = searchHistoryDao.observeHistory().first()
        assertTrue(history.isEmpty())
    }

    @Test
    fun `clearAll removes all entries`() = runBlocking {
        searchHistoryDao.upsert(SearchHistoryEntity(query = "a", searchedAt = 1000))
        searchHistoryDao.upsert(SearchHistoryEntity(query = "b", searchedAt = 2000))
        searchHistoryDao.clearAll()
        val history = searchHistoryDao.observeHistory().first()
        assertTrue(history.isEmpty())
    }

    @Test
    fun `trimTo keeps only latest N entries`() = runBlocking {
        searchHistoryDao.upsert(SearchHistoryEntity(query = "a", searchedAt = 1000))
        searchHistoryDao.upsert(SearchHistoryEntity(query = "b", searchedAt = 2000))
        searchHistoryDao.upsert(SearchHistoryEntity(query = "c", searchedAt = 3000))
        searchHistoryDao.trimTo(2)
        val history = searchHistoryDao.observeHistory().first()
        assertEquals(2, history.size)
        assertEquals(listOf("c", "b"), history.map { it.query })
    }
}