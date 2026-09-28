package mr.liks.feature.details.impl.data.mapper

import io.mockk.every
import io.mockk.mockk
import mr.liks.core.database.entity.DeveloperEntity
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.GenreEntity
import mr.liks.core.database.entity.PlatformEntity
import mr.liks.core.database.entity.PublisherEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity
import mr.liks.core.model.Developer
import mr.liks.core.model.Genre
import mr.liks.core.model.PlatformIcon
import mr.liks.core.model.Publisher
import mr.liks.core.model.Screenshot
import mr.liks.core.model.Trailer
import mr.liks.core.network.api.dto.DeveloperDto
import mr.liks.core.network.api.dto.GameDetailsDto
import mr.liks.core.network.api.dto.GenreDto
import mr.liks.core.network.api.dto.MovieDto
import mr.liks.core.network.api.dto.PlatformDto
import mr.liks.core.network.api.dto.PublisherDto
import mr.liks.core.network.api.dto.ScreenshotDto
import org.junit.Assert.assertEquals
import org.junit.Test

/** Тесты на мапперов из DetailsMappers.kt */
class DetailsMappersTest {
    @Test
    fun `GameDetailsDto toEntity maps fields`() {
        val dto = mockk<GameDetailsDto> {
            every { id } returns 1L
            every { description } returns "desc"
            every { descriptionRaw } returns "raw"
            every { website } returns "site"
            every { redditUrl } returns "reddit"
            every { metacriticUrl } returns "mc"
        }

        val entity = dto.toEntity()

        assertEquals(1L, entity.gameId)
        assertEquals("desc", entity.description)
        assertEquals("raw", entity.descriptionRaw)
        assertEquals("site", entity.website)
        assertEquals("reddit", entity.redditUrl)
        assertEquals("mc", entity.metacriticUrl)
    }

    @Test
    fun `DeveloperDto toEntity maps fields`() {
        val dto = mockk<DeveloperDto> {
            every { id } returns 1L
            every { name } returns "Dev"
            every { slug } returns "dev"
        }

        val entity = dto.toEntity()

        assertEquals(1L, entity.id)
        assertEquals("Dev", entity.name)
        assertEquals("dev", entity.slug)
    }

    @Test
    fun `PublisherDto toEntity maps fields`() {
        val dto = mockk<PublisherDto> {
            every { id } returns 2L
            every { name } returns "Pub"
            every { slug } returns "pub"
        }

        val entity = dto.toEntity()

        assertEquals(2L, entity.id)
        assertEquals("Pub", entity.name)
        assertEquals("pub", entity.slug)
    }

    @Test
    fun `GenreDto toEntity maps fields`() {
        val dto = mockk<GenreDto> {
            every { id } returns 3L
            every { name } returns "RPG"
            every { slug } returns "rpg"
        }

        val entity = dto.toEntity()

        assertEquals(3L, entity.id)
        assertEquals("RPG", entity.name)
        assertEquals("rpg", entity.slug)
    }

    @Test
    fun `PlatformDto toEntity maps fields`() {
        val dto = mockk<PlatformDto> {
            every { id } returns 4L
            every { name } returns "PC"
            every { slug } returns "pc"
            every { imageBackground } returns "icon"
        }

        val entity = dto.toEntity()

        assertEquals(4L, entity.id)
        assertEquals("PC", entity.name)
        assertEquals("pc", entity.slug)
        assertEquals("icon", entity.imageBackground)
    }

    @Test
    fun `MovieDto toEntity maps fields`() {
        val dto = mockk<MovieDto>(relaxed = true) {
            every { id } returns 10L
            every { name } returns "Trailer"
            every { preview } returns "preview"
            every { data?.data480 } returns "480"
            every { data?.max } returns "max"
        }

        val entity = dto.toEntity(gameId = 1L)

        assertEquals(10L, entity.id)
        assertEquals(1L, entity.gameId)
        assertEquals("Trailer", entity.name)
        assertEquals("preview", entity.preview)
        assertEquals("480", entity.data480)
        assertEquals("max", entity.dataMax)
    }

    @Test
    fun `ScreenshotDto toEntity maps fields`() {
        val dto = mockk<ScreenshotDto> {
            every { id } returns 11L
            every { image } returns "image"
            every { width } returns 1920
            every { height } returns 1080
            every { isDeleted } returns false
        }

        val entity = dto.toEntity(gameId = 1L)

        assertEquals(11L, entity.id)
        assertEquals(1L, entity.gameId)
        assertEquals("image", entity.image)
        assertEquals(1920, entity.width)
        assertEquals(1080, entity.height)
        assertEquals(false, entity.isDeleted)
    }

    @Test
    fun `TrailerEntity toDomain maps fields`() {
        val entity = TrailerEntity(
            id = 1L,
            gameId = 2L,
            name = "Trailer",
            preview = "preview",
            data480 = "480",
            dataMax = "max"
        )

        val domain = entity.toDomain()

        assertEquals(
            Trailer(
                id = 1L,
                name = "Trailer",
                preview = "preview",
                data480 = "480",
                dataMax = "max"
            ),
            domain
        )
    }

    @Test
    fun `ScreenshotEntity toDomain maps fields`() {
        val entity = ScreenshotEntity(
            id = 1L,
            gameId = 2L,
            image = "image",
            width = 1920,
            height = 1080,
            isDeleted = false
        )

        val domain = entity.toDomain()

        assertEquals(
            Screenshot(
                id = 1L,
                image = "image",
                width = 1920,
                height = 1080
            ),
            domain
        )
    }

    @Test
    fun `DeveloperEntity toDomain maps fields`() {
        val entity = DeveloperEntity(id = 1L, name = "Dev", slug = "dev")

        val domain = entity.toDomain()

        assertEquals(Developer(id = 1L, name = "Dev"), domain)
    }

    @Test
    fun `PublisherEntity toDomain maps fields`() {
        val entity = PublisherEntity(id = 1L, name = "Pub", slug = "pub")

        val domain = entity.toDomain()

        assertEquals(Publisher(id = 1L, name = "Pub"), domain)
    }

    @Test
    fun `GenreEntity toDomain maps fields`() {
        val entity = GenreEntity(id = 1L, name = "RPG", slug = "rpg")

        val domain = entity.toDomain()

        assertEquals(Genre(id = 1L, name = "RPG"), domain)
    }

    @Test
    fun `PlatformEntity toDomain maps fields`() {
        val entity = PlatformEntity(
            id = 1L,
            name = "PC",
            slug = "pc",
            imageBackground = "icon"
        )

        val domain = entity.toDomain()

        assertEquals(
            PlatformIcon(
                id = 1L,
                name = "PC",
                iconUrl = "icon"
            ),
            domain
        )
    }
}