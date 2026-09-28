package mr.liks.feature.details.impl.data

import androidx.room.withTransaction
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.database.RawgDatabase
import mr.liks.core.database.dao.GameDao
import mr.liks.core.database.dao.GameDetailsDao
import mr.liks.core.database.entity.GameDetailsEntity
import mr.liks.core.database.entity.GameEntity
import mr.liks.core.database.entity.ScreenshotEntity
import mr.liks.core.database.entity.TrailerEntity
import mr.liks.core.network.api.RawgApi
import mr.liks.core.network.api.dto.DeveloperDto
import mr.liks.core.network.api.dto.GameDetailsDto
import mr.liks.core.network.api.dto.GenreDto
import mr.liks.core.network.api.dto.MovieDto
import mr.liks.core.network.api.dto.PublisherDto
import mr.liks.core.network.api.dto.ScreenshotDto
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Тесты на [DetailsRepositoryImpl] */
@OptIn(ExperimentalCoroutinesApi::class)
class DetailsRepositoryImplTest {
    private val api = mockk<RawgApi>()
    private val database = mockk<RawgDatabase>(relaxed = true)
    private val gameDao = mockk<GameDao>(relaxed = true)
    private val gameDetailsDao = mockk<GameDetailsDao>(relaxed = true)

    @Before
    fun setUp() {
        every { database.gameDao() } returns gameDao
        every { database.gameDetailsDao() } returns gameDetailsDao

        mockkStatic("androidx.room.RoomDatabaseKt")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `refreshDetails upserts game details and relations`() = runTest {
        val repository = createRepository()

        val developerDto = mockk<DeveloperDto> {
            every { id } returns 1L
            every { name } returns "Dev"
            every { slug } returns "dev"
        }
        val publisherDto = mockk<PublisherDto> {
            every { id } returns 2L
            every { name } returns "Pub"
            every { slug } returns "pub"
        }
        val genreDto = mockk<GenreDto> {
            every { id } returns 3L
            every { name } returns "RPG"
            every { slug } returns "rpg"
        }

        val dto = mockk<GameDetailsDto>(relaxed = true) {
            every { id } returns 1L
            every { slug } returns "game"
            every { name } returns "Game"
            every { released } returns "2024-01-01"
            every { backgroundImage } returns "bg"
            every { rating } returns 4.5
            every { metacritic } returns 90
            every { playtime } returns 10
            every { description } returns "desc"
            every { descriptionRaw } returns "raw"
            every { website } returns "site"
            every { redditUrl } returns "reddit"
            every { metacriticUrl } returns "mc"
            every { developers } returns listOf(developerDto)
            every { publishers } returns listOf(publisherDto)
            every { genres } returns listOf(genreDto)
            every { platforms } returns emptyList()
        }

        coEvery { api.getGameDetails(1L) } returns dto

        repository.refreshDetails(1L)

        coVerify { gameDao.upsertGamePreservingFeedOrder(any()) }
        coVerify { gameDetailsDao.upsertDetails(any()) }
        coVerify { gameDao.upsertDevelopers(any()) }
        coVerify { gameDao.insertDeveloperCrossRefs(any()) }
        coVerify { gameDao.upsertPublishers(any()) }
        coVerify { gameDao.insertPublisherCrossRefs(any()) }
        coVerify { gameDao.upsertGenres(any()) }
        coVerify { gameDao.insertGenreCrossRefs(any()) }
    }

    @Test
    fun `refreshMedia replaces trailers and screenshots`() = runTest {
        val repository = createRepository()

        val movieDto = mockk<MovieDto>(relaxed = true) {
            every { id } returns 1L
            every { name } returns "Trailer"
            every { preview } returns "preview"
        }
        val screenshotDto = mockk<ScreenshotDto>(relaxed = true) {
            every { id } returns 2L
            every { image } returns "image"
            every { width } returns 1920
            every { height } returns 1080
            every { isDeleted } returns false
        }

        coEvery { api.getGameTrailers(1L) } returns mockk {
            every { results } returns listOf(movieDto)
        }
        coEvery { api.getGameScreenshots(1L) } returns mockk {
            every { results } returns listOf(screenshotDto)
        }

        repository.refreshMedia(1L)

        coVerify { gameDetailsDao.clearTrailers(1L) }
        coVerify { gameDetailsDao.upsertTrailers(any()) }
        coVerify { gameDetailsDao.clearScreenshots(1L) }
        coVerify { gameDetailsDao.upsertScreenshots(any()) }
    }

    @Test
    fun `refreshMedia swallows network errors`() = runTest {
        val repository = createRepository()

        coEvery { api.getGameTrailers(1L) } throws RuntimeException("network")
        coEvery { api.getGameScreenshots(1L) } throws RuntimeException("network")

        repository.refreshMedia(1L)

        coVerify(exactly = 0) { gameDetailsDao.clearTrailers(any()) }
        coVerify(exactly = 0) { gameDetailsDao.clearScreenshots(any()) }
    }

    @Test
    fun `observeMedia maps entities to domain`() = runTest {
        val repository = createRepository()

        val trailer = TrailerEntity(
            id = 1L,
            gameId = 1L,
            name = "Trailer",
            preview = "preview",
            data480 = "480",
            dataMax = "max"
        )
        val screenshot = ScreenshotEntity(
            id = 2L,
            gameId = 1L,
            image = "image",
            width = 1920,
            height = 1080,
            isDeleted = false
        )

        every { gameDetailsDao.observeTrailers(1L) } returns flowOf(listOf(trailer))
        every { gameDetailsDao.observeScreenshots(1L) } returns flowOf(listOf(screenshot))

        repository.observeMedia(1L).test {
            val media = awaitItem()

            assertEquals(1, media.trailers.size)
            assertEquals("Trailer", media.trailers.first().name)
            assertEquals(1, media.screenshots.size)
            assertEquals("image", media.screenshots.first().image)

            awaitComplete()
        }
    }

    @Test
    fun `observeDetails returns null when game or details missing`() = runTest {
        val repository = createRepository()

        every { gameDao.observeById(1L) } returns flowOf(null)
        every { gameDetailsDao.observeDetails(1L) } returns flowOf(null)
        every { gameDao.observeWithRelations(1L) } returns flowOf(null)

        repository.observeDetails(1L).test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeDetails maps basic game and details fields`() = runTest {
        val repository = createRepository()

        val game = GameEntity(
            id = 1L,
            slug = "slug",
            name = "Game",
            released = "2024-01-01",
            backgroundImage = "bg",
            rating = 4.5,
            ratingsCount = 0,
            metacritic = 90,
            playtime = 10,
            feedOrder = 0
        )
        val details = GameDetailsEntity(
            gameId = 1L,
            description = "desc",
            descriptionRaw = "raw",
            website = "site",
            redditUrl = "reddit",
            metacriticUrl = "mc"
        )

        every { gameDao.observeById(1L) } returns flowOf(game)
        every { gameDetailsDao.observeDetails(1L) } returns flowOf(details)
        every { gameDao.observeWithRelations(1L) } returns flowOf(null)

        repository.observeDetails(1L).test {
            val result = awaitItem()

            assertNotNull(result)
            assertEquals("Game", result!!.name)
            assertEquals("desc", result.description)
            assertTrue(result.genres.isEmpty())
            assertTrue(result.platforms.isEmpty())

            awaitComplete()
        }
    }

    private fun TestScope.createRepository(): DetailsRepositoryImpl {
        coEvery {
            database.withTransaction(captureCoroutine<suspend () -> Unit>())
        } coAnswers {
            coroutine<suspend () -> Unit>().captured.invoke()
        }
        val dispatchers = mockk<DispatchersProvider> {
            every { io } returns StandardTestDispatcher(testScheduler)
        }
        return DetailsRepositoryImpl(
            api = api,
            database = database,
            dispatchers = dispatchers
        )
    }
}