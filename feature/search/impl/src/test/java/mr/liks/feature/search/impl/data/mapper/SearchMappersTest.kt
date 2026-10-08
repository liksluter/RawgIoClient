package mr.liks.feature.search.impl.data.mapper

import io.mockk.every
import io.mockk.mockk
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.SearchHistoryEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.GenreDto
import mr.liks.core.network.api.dto.PlatformDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SearchMappersTest {
    @Test
    fun `GameListDto toEntity maps fields`() {
        val dto = mockk<GameListDto> {
            every { id } returns 1L
            every { slug } returns "slug"
            every { name } returns "Name"
            every { released } returns "2024-01-01"
            every { backgroundImage } returns "url"
            every { rating } returns 4.5
            every { ratingsCount } returns 10
            every { metacritic } returns 90
            every { playtime } returns 20
        }

        val entity = dto.toEntity()

        assertEquals(1L, entity.id)
        assertEquals("slug", entity.slug)
        assertEquals("Name", entity.name)
        assertEquals("2024-01-01", entity.released)
        assertEquals("url", entity.backgroundImage)
        assertEquals(4.5, entity.rating, 0.0)
        assertEquals(10, entity.ratingsCount)
        assertEquals(90, entity.metacritic)
        assertEquals(20, entity.playtime)
    }

    @Test
    fun `GameListDto toSearchEntry maps query and position`() {
        val dto = mockk<GameListDto> {
            every { id } returns 2L
        }

        val entity = dto.toSearchEntry("query", 3)

        assertEquals("query", entity.query)
        assertEquals(2L, entity.gameId)
        assertEquals(3, entity.position)
    }

    @Test
    fun `PlatformDto toEntity maps fields`() {
        val dto = mockk<PlatformDto> {
            every { id } returns 1L
            every { name } returns "PC"
            every { slug } returns "pc"
            every { image } returns "img"
        }

        val entity = dto.toEntity()

        assertEquals(1L, entity.id)
        assertEquals("PC", entity.name)
        assertEquals("pc", entity.slug)
        assertEquals("img", entity.image)
    }

    @Test
    fun `GenreDto toEntity maps fields`() {
        val dto = mockk<GenreDto> {
            every { id } returns 1L
            every { name } returns "Action"
            every { slug } returns "action"
            every { gamesCount } returns 10
        }

        val entity = dto.toEntity()

        assertEquals(1L, entity.id)
        assertEquals("Action", entity.name)
        assertEquals("action", entity.slug)
        assertEquals(10, entity.gamesCount)
    }

    @Test
    fun `GameWithPropertiesRelation toDomain maps platforms and genres`() {
        val gameEntity = GameEntity(
            id = 1L,
            slug = "slug",
            name = "Game",
            released = "2024-01-01",
            backgroundImage = "url",
            rating = 4.0,
            ratingsCount = 5,
            metacritic = 80,
            playtime = 10
        )
        val platforms = listOf(PlatformEntity(1L, "PC", "pc", "img"))
        val genres = listOf(GenreEntity(1L, "Action", "action", 10))

        val relation = mockk<GameWithPropertiesRelation> {
            every { game } returns gameEntity
            every { this@mockk.platforms } returns platforms
            every { this@mockk.genres } returns genres
        }

        val domain = relation.toDomain()

        assertEquals(1L, domain.id)
        assertEquals("Game", domain.name)
        assertEquals("PC", domain.platformsNames)
        assertEquals("Action", domain.genres)
    }

    @Test
    fun `GameWithPropertiesRelation toDomain sets null when empty`() {
        val gameEntity = GameEntity(
            id = 1L,
            slug = "slug",
            name = "Game",
            released = null,
            backgroundImage = null,
            rating = 0.0,
            ratingsCount = 0,
            metacritic = null,
            playtime = null
        )

        val relation = mockk<GameWithPropertiesRelation> {
            every { game } returns gameEntity
            every { this@mockk.platforms } returns emptyList()
            every { this@mockk.genres } returns emptyList()
        }

        val domain = relation.toDomain()

        assertNull(domain.platformsNames)
        assertNull(domain.genres)
    }

    @Test
    fun `PlatformEntity toDomain`() {
        val entity = PlatformEntity(1L, "PC", "pc", "img")
        val domain = entity.toDomain()

        assertEquals(1L, domain.id)
        assertEquals("PC", domain.name)
        assertEquals("img", domain.image)
    }

    @Test
    fun `SearchHistoryEntity toDomain`() {
        val entity = SearchHistoryEntity(id = 1L, query = "query", searchedAt = 123L)
        val domain = entity.toDomain()

        assertEquals(1L, domain.id)
        assertEquals("query", domain.query)
        assertEquals(123L, domain.searchedAt)
    }

    @Test
    fun `GenreEntity toDomain`() {
        val entity = GenreEntity(1L, "Action", "action", 10)
        val domain = entity.toDomain()

        assertEquals(1L, domain.id)
        assertEquals("Action", domain.name)
    }
}