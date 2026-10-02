package mr.liks.feature.feed.impl.data.mapper

import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.GamePlatformCrossRef
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.relation.GameWithPropertiesRelation
import mr.liks.core.network.api.dto.GameListDto
import mr.liks.core.network.api.dto.PlatformDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

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
        val entity = dto.toEntity()

        assertEquals(42L, entity.id)
        assertEquals("game-slug", entity.slug)
        assertEquals("Game Name", entity.name)
        assertEquals("2023-05-20", entity.released)
        assertEquals("https://img.com/bg.jpg", entity.backgroundImage)
        assertEquals(4.7, entity.rating, 0.001)
        assertEquals(1500, entity.ratingsCount)
        assertEquals(88, entity.metacritic)
        assertEquals(25, entity.playtime)
    }

    @Test
    fun `GameListDto toFeedEntry uses ordering and negative sort value`() {
        val dto = GameListDto(
            id = 42L,
            slug = "s",
            name = "n",
            released = "2023-05-20T10:00:00Z",
            backgroundImage = null,
            rating = 4.7,
            ratingsCount = 0,
            metacritic = 88,
            playtime = 0,
            platforms = emptyList()
        )

        val byRating = dto.toFeedEntry("-rating")
        assertEquals("-rating", byRating.ordering)
        assertEquals(42L, byRating.gameId)
        assertEquals(-4.7, byRating.sortValue, 0.001)
        assertEquals(42L, byRating.secondarySort)

        val byMeta = dto.toFeedEntry("-metacritic")
        assertEquals(-88.0, byMeta.sortValue, 0.001)

        val byReleased = dto.toFeedEntry("-released")
        assertEquals(-Instant.parse("2023-05-20T10:00:00Z").epochSecond.toDouble(),
            byReleased.sortValue, 0.001)
    }

    @Test
    fun `GameListDto toFeedEntry falls back to rating for unknown ordering`() {
        val dto = GameListDto(
            id = 1L, slug = "s", name = "n", released = null,
            backgroundImage = null, rating = 3.0, ratingsCount = 0,
            metacritic = null, playtime = 0, platforms = emptyList()
        )
        val entry = dto.toFeedEntry("unknown")
        assertEquals(-3.0, entry.sortValue, 0.001)
    }

    @Test
    fun `GameListDto toFeedEntry handles null metacritic and released`() {
        val dto = GameListDto(
            id = 1L, slug = "s", name = "n", released = null,
            backgroundImage = null, rating = 0.0, ratingsCount = 0,
            metacritic = null, playtime = 0, platforms = emptyList()
        )
        assertEquals(0.0, dto.toFeedEntry("-metacritic").sortValue, 0.001)
        assertEquals(0.0, dto.toFeedEntry("-released").sortValue, 0.001)
    }

    @Test
    fun `PlatformDto toEntity maps fields and nulls image`() {
        val dto = PlatformDto(
            id = 10L,
            name = "PlayStation 5",
            slug = "ps5",
            image = "https://img.com/ps5.png"
        )
        val entity = dto.toEntity()

        assertEquals(10L, entity.id)
        assertEquals("PlayStation 5", entity.name)
        assertEquals("ps5", entity.slug)
        assertNull(entity.image)
    }

    @Test
    fun `platformCrossRef creates correct cross ref`() {
        val ref = GamePlatformCrossRef(gameId = 1L, platformId = 2L, releasedAt = "2023-01-01")
        assertEquals(1L, ref.gameId)
        assertEquals(2L, ref.platformId)
        assertEquals("2023-01-01", ref.releasedAt)
    }

    @Test
    fun `platformCrossRef with null releasedAt`() {
        val ref = GamePlatformCrossRef(gameId = 1L, platformId = 2L, releasedAt = null)
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
            playtime = 5
        )
        val platformEntity = PlatformEntity(
            id = 1L,
            name = "PC",
            slug = "pc",
            image = "icon"
        )
        val relation = GameWithPropertiesRelation(
            game = gameEntity,
            platforms = listOf(platformEntity),
            genres = emptyList(),
            developers = emptyList(),
            publishers = emptyList()
        )

        val domain = relation.toDomain()

        assertEquals(5L, domain.id)
        assertEquals("Name", domain.name)
        assertEquals("bg", domain.backgroundImage)
        assertEquals(4.0, domain.rating, 0.001)
        assertEquals(1, domain.platforms.size)
        assertEquals("PC", domain.platforms[0].name)
        assertEquals("PC", domain.platformsNames)
        assertNull(domain.trailerUrl)
        assertNull(domain.trailerPreview)
    }

    @Test
    fun `GameWithPropertiesRelation toDomain with empty platforms gives blank names`() {
        val relation = GameWithPropertiesRelation(
            game = GameEntity(
                id = 1L, slug = "s", name = "n", released = null,
                backgroundImage = null, rating = 0.0, ratingsCount = 0,
                metacritic = null, playtime = 0
            ),
            platforms = emptyList(),
            genres = emptyList(),
            developers = emptyList(),
            publishers = emptyList()
        )
        assertEquals("", relation.toDomain().platformsNames)
    }

    @Test
    fun `PlatformEntity toDomain maps correctly`() {
        val entity = PlatformEntity(
            id = 7L,
            name = "Xbox",
            slug = "xbox",
            image = "xbox.png"
        )
        val domain = entity.toDomain()

        assertEquals(7L, domain.id)
        assertEquals("Xbox", domain.name)
        assertEquals("xbox.png", domain.image)
    }
}