package mr.liks.core.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Реализация [TrailerPlayerController]
 *
 * @property context контекст приложения
 */
@OptIn(UnstableApi::class)
internal class TrailerPlayerControllerImpl(
    private val context: Context
) : TrailerPlayerController {
    private var player: ExoPlayer? = null
    private var currentUrl: String? = null

    override fun bindTo(view: PlayerView, url: String) {
        val existing = player
        val target = if (existing != null && currentUrl == url) {
            existing
        } else {
            existing?.release()
            ExoPlayerFactory.createDetailPlayer(context).apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = true
                player = this
                currentUrl = url
            }
        }

        view.useController = true
        view.setShowNextButton(false)
        view.setShowPreviousButton(false)
        view.setShowFastForwardButton(true)
        view.setShowRewindButton(true)
        view.player = target
    }

    override fun unbind() {
        player?.pause()
    }

    override fun pause() { player?.pause() }
    override fun resume() { player?.play() }

    override fun release() {
        player?.release()
        player = null
        currentUrl = null
    }
}