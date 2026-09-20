package mr.liks.core.media

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.RuntimeEnvironment
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

/** Тесты на [ExoPlayerFactory] */
@ExtendWith(RobolectricExtension::class)
class ExoPlayerFactoryTest {
    private lateinit var context: Context

    @BeforeEach
    fun setup() {
        context = RuntimeEnvironment.getApplication()
    }

    @Test
    fun `createFeedPlayer sets correct settings`() {
        val player = ExoPlayerFactory.createFeedPlayer(context)
        assertNotNull(player)
        assertEquals(0f, player.volume)
        assertEquals(Player.REPEAT_MODE_ONE, player.repeatMode)
        assertTrue(player.playWhenReady)

        val audioAttributes = player.audioAttributes
        assertEquals(C.USAGE_MEDIA, audioAttributes.usage)
        assertEquals(C.AUDIO_CONTENT_TYPE_MOVIE, audioAttributes.contentType)

        player.release()
    }

    @Test
    fun `createDetailPlayer sets correct settings`() {
        val player = ExoPlayerFactory.createDetailPlayer(context)
        assertNotNull(player)
        assertEquals(Player.REPEAT_MODE_OFF, player.repeatMode)
        assertFalse(player.playWhenReady)

        val audioAttributes = player.audioAttributes
        assertEquals(C.USAGE_MEDIA, audioAttributes.usage)
        assertEquals(C.AUDIO_CONTENT_TYPE_MOVIE, audioAttributes.contentType)

        player.release()
    }
}