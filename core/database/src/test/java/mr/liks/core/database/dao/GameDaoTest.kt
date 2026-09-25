package mr.liks.core.database.dao

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GameGenreCrossRef
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/** Тесты для [GameDao] */
class GameDaoTest : DatabaseTest() {
    private val gameDao get() = db.gameDao()

    @Test
    fun `upsertGames inserts games`() = runBlocking {
        val entity = game(
            id = 1,
            name = "Game 1",
            slug = "game-1",
            rating = 4.5
        ).copy(
            released = "2023-01-01",
            backgroundImage = "url",
            ratingsCount = 100,
            metacritic = 85,
            playtime = 10,
            feedOrder = 0
        )
        gameDao.upsertGames(listOf(entity))
        val loaded = gameDao.observeById(1).first()
        assertNotNull(loaded)
        assertEquals(entity, loaded)
    }

    @Test
    fun `upsertGame updates existing game`() = runBlocking {
        val entity = game(id = 1, name = "Game 1", slug = "game-1")
        gameDao.upsertGame(entity)
        val updated = entity.copy(name = "Updated Game")
        gameDao.upsertGame(updated)
        val loaded = gameDao.observeById(1).first()
        assertEquals("Updated Game", loaded?.name)
    }

    @Test
    fun `observeWithRelations returns game with platforms and genres`() = runBlocking {
        val entity = game(id = 1, name = "Game 1", slug = "game-1")
        val platform = PlatformEntity(1, "PC", "pc", "img")
        val genre = GenreEntity(1, "Action", "action")
        gameDao.upsertGame(entity)
        gameDao.upsertPlatforms(listOf(platform))
        gameDao.upsertGenres(listOf(genre))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, "2023-01-01")))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))

        val relation = gameDao.observeWithRelations(1).first()
        assertNotNull(relation)
        assertEquals(entity, relation?.game)
        assertEquals(listOf(platform), relation?.platforms)
        assertEquals(listOf(genre), relation?.genres)
    }

    @Test
    fun `deletePlatformRefsFor removes cross refs`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDao.upsertPlatforms(listOf(PlatformEntity(1, "PC", "pc", null)))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, null)))
        gameDao.deletePlatformRefsFor(listOf(1))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.platforms?.isEmpty() == true)
    }

    @Test
    fun `deleteGenreRefsFor removes cross refs`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDao.upsertGenres(listOf(GenreEntity(1, "Action", "action")))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))
        gameDao.deleteGenreRefsFor(listOf(1))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.genres?.isEmpty() == true)
    }

    @Test
    fun `clearGames removes all games`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDao.clearGames()
        assertNull(gameDao.observeById(1).first())
    }

    @Test
    fun `clearPlatforms removes all platforms`() = runBlocking {
        gameDao.upsertPlatforms(listOf(PlatformEntity(1, "PC", "pc", null)))
        gameDao.clearPlatforms()
        gameDao.upsertGame(game(1))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, null)))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.platforms?.isEmpty() == true)
    }

    @Test
    fun `clearGenres removes all genres`() = runBlocking {
        gameDao.upsertGenres(listOf(GenreEntity(1, "Action", "action")))
        gameDao.clearGenres()
        gameDao.upsertGame(game(1))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.genres?.isEmpty() == true)
    }

    @Test
    fun `clearPlatformCrossRefs removes all platform refs`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDao.upsertPlatforms(listOf(PlatformEntity(1, "PC", "pc", null)))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, null)))
        gameDao.clearPlatformCrossRefs()
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.platforms?.isEmpty() == true)
    }

    @Test
    fun `clearGenreCrossRefs removes all genre refs`() = runBlocking {
        gameDao.upsertGame(game(1))
        gameDao.upsertGenres(listOf(GenreEntity(1, "Action", "action")))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))
        gameDao.clearGenreCrossRefs()
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.genres?.isEmpty() == true)
    }

    @Test
    fun `count returns number of games`() = runBlocking {
        assertEquals(0L, gameDao.count())
        gameDao.upsertGames(listOf(game(1), game(2), game(3)))
        assertEquals(3L, gameDao.count())
        gameDao.clearGames()
        assertEquals(0L, gameDao.count())
    }

    @Test
    fun `pagingSource returns games ordered by feedOrder ASC`() = runBlocking {
        val g1 = game(id = 1, feedOrder = 30)
        val g2 = game(id = 2, feedOrder = 10)
        val g3 = game(id = 3, feedOrder = 20)
        gameDao.upsertGames(listOf(g1, g2, g3))

        val ids = loadRefresh(gameDao.pagingSource()).map { it.game.id }
        assertEquals(listOf(2L, 3L, 1L), ids)
    }

    @Test
    fun `pagingSource order stays stable after re-upsert`() = runBlocking {
        val g1 = game(id = 1, feedOrder = 1, updatedAt = 1_000)
        val g2 = game(id = 2, feedOrder = 2, updatedAt = 2_000)
        gameDao.upsertGames(listOf(g1, g2))

        gameDao.upsertGame(g1.copy(updatedAt = 9_999))

        val ids = loadRefresh(gameDao.pagingSource()).map { it.game.id }
        assertEquals(listOf(1L, 2L), ids)
    }

    @Test
    fun `searchPagingSource filters and orders by rating DESC, id DESC`() = runBlocking {
        val g1 = game(id = 1, feedOrder = 1, name = "Game One", slug = "s1", rating = 3.0)
        val g2 = game(id = 2, feedOrder = 2, name = "Game Two", slug = "s2", rating = 5.0)
        val g3 = game(id = 3, feedOrder = 3, name = "Other",    slug = "s3", rating = 4.0)
        gameDao.upsertGames(listOf(g1, g2, g3))

        val ids = loadRefresh(gameDao.searchPagingSource("Game")).map { it.id }
        assertEquals(listOf(2L, 1L), ids)
    }

    private fun game(
        id: Long,
        feedOrder: Long = id,
        name: String = "Game $id",
        slug: String = "game-$id",
        rating: Double = 0.0,
        updatedAt: Long = System.currentTimeMillis()
    ): GameEntity = GameEntity(
        id = id,
        slug = slug,
        name = name,
        released = null,
        backgroundImage = null,
        rating = rating,
        ratingsCount = 0,
        metacritic = null,
        playtime = null,
        feedOrder = feedOrder,
        updatedAt = updatedAt
    )

    private fun <T : Any> loadRefresh(source: PagingSource<Int, T>): List<T> = runBlocking {
        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 50,
                placeholdersEnabled = false
            )
        )
        assertTrue(result is PagingSource.LoadResult.Page)
        (result as PagingSource.LoadResult.Page).data
    }
}