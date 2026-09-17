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
        val game = GameEntity(
            id = 1,
            slug = "game-1",
            name = "Game 1",
            released = "2023-01-01",
            backgroundImage = "url",
            rating = 4.5,
            ratingsCount = 100,
            metacritic = 85,
            playtime = 10
        )
        gameDao.upsertGames(listOf(game))
        val loaded = gameDao.observeById(1).first()
        assertNotNull(loaded)
        assertEquals(game, loaded)
    }

    @Test
    fun `upsertGame updates existing game`() = runBlocking {
        val game = GameEntity(
            id = 1,
            slug = "game-1",
            name = "Game 1",
            released = "2023-01-01",
            backgroundImage = "url",
            rating = 4.5,
            ratingsCount = 100,
            metacritic = 85,
            playtime = 10
        )
        gameDao.upsertGame(game)
        val updated = game.copy(name = "Updated Game")
        gameDao.upsertGame(updated)
        val loaded = gameDao.observeById(1).first()
        assertEquals("Updated Game", loaded?.name)
    }

    @Test
    fun `observeWithRelations returns game with platforms and genres`() = runBlocking {
        val game = GameEntity(
            id = 1,
            slug = "game-1",
            name = "Game 1",
            released = "2023-01-01",
            backgroundImage = "url",
            rating = 4.5,
            ratingsCount = 100,
            metacritic = 85,
            playtime = 10
        )
        val platform = PlatformEntity(1, "PC", "pc", "img")
        val genre = GenreEntity(1, "Action", "action")
        gameDao.upsertGame(game)
        gameDao.upsertPlatforms(listOf(platform))
        gameDao.upsertGenres(listOf(genre))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, "2023-01-01")))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))

        val relation = gameDao.observeWithRelations(1).first()
        assertNotNull(relation)
        assertEquals(game, relation?.game)
        assertEquals(listOf(platform), relation?.platforms)
        assertEquals(listOf(genre), relation?.genres)
    }

    @Test
    fun `deletePlatformRefsFor removes cross refs`() = runBlocking {
        val game = GameEntity(1, "slug", "name", null, null, 0.0, 0, null, null)
        val platform = PlatformEntity(1, "PC", "pc", null)
        gameDao.upsertGame(game)
        gameDao.upsertPlatforms(listOf(platform))
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, null)))
        gameDao.deletePlatformRefsFor(listOf(1))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.platforms?.isEmpty() == true)
    }

    @Test
    fun `deleteGenreRefsFor removes cross refs`() = runBlocking {
        val game = GameEntity(1, "slug", "name", null, null, 0.0, 0, null, null)
        val genre = GenreEntity(1, "Action", "action")
        gameDao.upsertGame(game)
        gameDao.upsertGenres(listOf(genre))
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))
        gameDao.deleteGenreRefsFor(listOf(1))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.genres?.isEmpty() == true)
    }

    @Test
    fun `clearGames removes all games`() = runBlocking {
        gameDao.upsertGame(GameEntity(1, "slug", "name", null, null, 0.0, 0, null, null))
        gameDao.clearGames()
        assertNull(gameDao.observeById(1).first())
    }

    @Test
    fun `clearPlatforms removes all platforms`() = runBlocking {
        gameDao.upsertPlatforms(listOf(PlatformEntity(1, "PC", "pc", null)))
        gameDao.clearPlatforms()
        val game = GameEntity(1, "slug", "name", null, null, 0.0, 0, null, null)
        gameDao.upsertGame(game)
        gameDao.insertPlatformCrossRefs(listOf(GamePlatformCrossRef(1, 1, null)))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.platforms?.isEmpty() == true)
    }

    @Test
    fun `clearGenres removes all genres`() = runBlocking {
        gameDao.upsertGenres(listOf(GenreEntity(1, "Action", "action")))
        gameDao.clearGenres()
        val game = GameEntity(1, "slug", "name", null, null, 0.0, 0, null, null)
        gameDao.upsertGame(game)
        gameDao.insertGenreCrossRefs(listOf(GameGenreCrossRef(1, 1)))
        val relation = gameDao.observeWithRelations(1).first()
        assertTrue(relation?.genres?.isEmpty() == true)
    }

    @Test
    fun `pagingSource returns games ordered by updatedAt DESC, id DESC`() = runBlocking {
        val game1 = GameEntity(1, "s1", "G1", null, null, 0.0, 0, null, null, updatedAt = 1000)
        val game2 = GameEntity(2, "s2", "G2", null, null, 0.0, 0, null, null, updatedAt = 2000)
        val game3 = GameEntity(3, "s3", "G3", null, null, 0.0, 0, null, null, updatedAt = 2000)
        gameDao.upsertGames(listOf(game1, game2, game3))

        val pagingSource = gameDao.pagingSource()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(listOf(3L, 2L, 1L), page.data.map { it.id })
    }

    @Test
    fun `searchPagingSource filters and orders by rating DESC, id DESC`() = runBlocking {
        val game1 = GameEntity(1, "s1", "Game One", null, null, 3.0, 0, null, null)
        val game2 = GameEntity(2, "s2", "Game Two", null, null, 5.0, 0, null, null)
        val game3 = GameEntity(3, "s3", "Other", null, null, 4.0, 0, null, null)
        gameDao.upsertGames(listOf(game1, game2, game3))

        val pagingSource = gameDao.searchPagingSource("Game")
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        assertEquals(listOf(2L, 1L), page.data.map { it.id })
    }
}