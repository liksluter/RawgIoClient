package mr.liks.core.database.dao

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

/** Тесты для [GameDetailsDao] */
class GameDetailsDaoTest : DatabaseTest() {
    private val gameDao get() = db.gameDao()
    private val gameDetailsDao get() = db.gameDetailsDao()

    @Test
    fun `upsertDetails inserts details`() = runBlocking {
        gameDao.upsertGame(game(1))
        val details = GameDetailsEntity(
            gameId = 1,
            description = "desc",
            descriptionRaw = "raw",
            website = "web",
            redditUrl = "reddit",
            metacriticUrl = "meta"
        )
        gameDetailsDao.upsertDetails(details)
        val loaded = gameDetailsDao.observeDetails(1).first()
        assertEquals(details, loaded)
    }

    @Test
    fun `observeDetailsWithMedia returns details with trailers and screenshots`() = runBlocking {
        gameDao.upsertGame(game(1))
        val details = GameDetailsEntity(1, "desc", "raw", "web", "reddit", "meta")
        gameDetailsDao.upsertDetails(details)
        val trailer = TrailerEntity(1, 1, "trailer", "preview", "480", "max")
        val screenshot = ScreenshotEntity(1, 1, "image", 100, 100, false)
        gameDetailsDao.upsertTrailers(listOf(trailer))
        gameDetailsDao.upsertScreenshots(listOf(screenshot))

        val relation = gameDetailsDao.observeDetailsWithMedia(1).first()
        assertNotNull(relation)
        assertEquals(details, relation?.details)
        assertEquals(listOf(trailer), relation?.trailers)
        assertEquals(listOf(screenshot), relation?.screenshots)
    }

    @Test
    fun `observeTrailers returns trailers ordered by id`() = runBlocking {
        gameDao.upsertGame(game(1))
        val t1 = TrailerEntity(2, 1, "t2", null, null, null)
        val t2 = TrailerEntity(1, 1, "t1", null, null, null)
        gameDetailsDao.upsertTrailers(listOf(t1, t2))
        val trailers = gameDetailsDao.observeTrailers(1).first()
        assertEquals(listOf(t2, t1), trailers)
    }

    @Test
    fun `observeScreenshots returns only non-deleted ordered by id`() = runBlocking {
        gameDao.upsertGame(game(1))
        val s1 = ScreenshotEntity(1, 1, "img1", 100, 100, false)
        val s2 = ScreenshotEntity(2, 1, "img2", 100, 100, true)
        val s3 = ScreenshotEntity(3, 1, "img3", 100, 100, false)
        gameDetailsDao.upsertScreenshots(listOf(s1, s2, s3))
        val screenshots = gameDetailsDao.observeScreenshots(1).first()
        assertEquals(listOf(s1, s3), screenshots)
    }

    @Test
    fun `clearTrailers removes trailers for game`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDetailsDao.upsertTrailers(listOf(TrailerEntity(1, 1, "t", null, null, null)))
        gameDetailsDao.clearTrailers(1)
        val trailers = gameDetailsDao.observeTrailers(1).first()
        assertTrue(trailers.isEmpty())
    }

    @Test
    fun `clearScreenshots removes screenshots for game`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDetailsDao.upsertScreenshots(listOf(ScreenshotEntity(1, 1, "img", 100, 100, false)))
        gameDetailsDao.clearScreenshots(1)
        val screenshots = gameDetailsDao.observeScreenshots(1).first()
        assertTrue(screenshots.isEmpty())
    }

    @Test
    fun `clearDetails removes details for game`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDetailsDao.upsertDetails(GameDetailsEntity(1, "desc", null, null, null, null))
        gameDetailsDao.clearDetails(1)
        val details = gameDetailsDao.observeDetails(1).first()
        assertNull(details)
    }

    private fun game(id: Long = 1, feedOrder: Long = id) = GameEntity(
        id = id,
        slug = "game-$id",
        name = "Game $id",
        released = null,
        backgroundImage = null,
        rating = 0.0,
        ratingsCount = 0,
        metacritic = null,
        playtime = null,
        feedOrder = feedOrder
    )
}