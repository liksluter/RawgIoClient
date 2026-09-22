package mr.liks.feature.feed.impl.data.mapper

import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.PlatformDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FeedMappersTest {

    @Test
    fun `GameListDto toEntity maps all fields`() {
        val dto = GameListDto(
            id = 42L,
            slug = "game-slug",
            name = "Game Name",
            released = "2023-05-20",
            backgroundImage = "https://img.com/bg.jpg",
            rating = 4.7,
            ratingsCount = 1500,
            metacritic = 88,
            playtime = 25,
            platforms = emptyList()
        )
        val entity = dto.toEntity(feedOrder = 7L)

        assertEquals(42L, entity.id)
        assertEquals("game-slug", entity.slug)
        assertEquals("Game Name", entity.name)
        assertEquals("2023-05-20", entity.released)
        assertEquals("https://img.com/bg.jpg", entity.backgroundImage)
        assertEquals(4.7, entity.rating, 0.001)
        assertEquals(1500, entity.ratingsCount)
        assertEquals(88, entity.metacritic)
        assertEquals(7L, entity.feedOrder)
        assertEquals(25, entity.playtime)
    }

    @Test
    fun `PlatformDto toEntity maps all fields`() {
        val dto = PlatformDto(
            id = 10L,
            name = "PlayStation 5",
            slug = "ps5",
            imageBackground = "https://img.com/ps5.png"
        )
        val entity = dto.toEntity()

        assertEquals(10L, entity.id)
        assertEquals("PlayStation 5", entity.name)
        assertEquals("ps5", entity.slug)
        assertEquals("https://img.com/ps5.png", entity.imageBackground)
    }

    @Test
    fun `platformCrossRef creates correct cross ref`() {
        val ref = platformCrossRef(gameId = 1L, platformId = 2L, releasedAt = "2023-01-01")

        assertEquals(1L, ref.gameId)
        assertEquals(2L, ref.platformId)
        assertEquals("2023-01-01", ref.releasedAt)
    }

    @Test
    fun `platformCrossRef with null releasedAt`() {
        val ref = platformCrossRef(gameId = 1L, platformId = 2L, releasedAt = null)
        assertNull(ref.releasedAt)
    }

    @Test
    fun `GameWithPropertiesRelation toDomain maps correctly`() {
        val gameEntity = GameEntity(
            id = 5L,
            slug = "slug",
            name = "Name",
            released = "2022-01-01",
            backgroundImage = "bg",
            rating = 4.0,
            ratingsCount = 10,
            metacritic = 70,
            feedOrder = 3L,
            playtime = 5
        )
        val platformEntity = PlatformEntity(
            id = 1L,
            name = "PC",
            slug = "pc",
            imageBackground = "icon"
        )
        val relation = GameWithPropertiesRelation(
            game = gameEntity,
            platforms = listOf(platformEntity),
            genres = emptyList()
        )

        val domain = relation.toDomain()

        assertEquals(5L, domain.id)
        assertEquals("Name", domain.name)
        assertEquals("bg", domain.backgroundImage)
        assertEquals(4.0, domain.rating, 0.001)
        assertEquals(1, domain.platforms.size)
        assertEquals("PC", domain.platforms[0].name)
        assertEquals("PC", domain.platformsNames)
        assertEquals(3L, domain.feedOrder)
        assertNull(domain.trailerUrl)
        assertNull(domain.trailerPreview)
    }

    @Test
    fun `PlatformEntity toDomain maps correctly`() {
        val entity = PlatformEntity(
            id = 7L,
            name = "Xbox",
            slug = "xbox",
            imageBackground = "xbox.png"
        )
        val domain = entity.toDomain()

        assertEquals(7L, domain.id)
        assertEquals("Xbox", domain.name)
        assertEquals("xbox.png", domain.iconUrl)
    }
}