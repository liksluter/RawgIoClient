package mr.liks.core.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.lang.ref.WeakReference

/**
 * Реализация [TrailerPlayerController]
 *
 * @property context контекст приложения
 */
@OptIn(UnstableApi::class)
internal class TrailerPlayerControllerImpl(
    context: Context
) : TrailerPlayerController {
    private val appContext = context.applicationContext

    private var player: ExoPlayer? = null
    private var currentUrl: String? = null
    private var attachedViewRef: WeakReference<PlayerView>? = null

    private fun playerOrCreate(): ExoPlayer =
        player ?: ExoPlayerFactory.createDetailPlayer(appContext).also { player = it }

    override fun bindTo(view: PlayerView, url: String) {
        val p = playerOrCreate()
        val attached = attachedViewRef?.get()

        if (attached === view && currentUrl == url) return

        if (attached !== view) {
            attached?.player = null
            attachedViewRef = WeakReference(view)
            view.player = p
        }

        if (currentUrl != url) {
            currentUrl = url
            p.setMediaItem(MediaItem.fromUri(url))
            p.prepare()
            p.playWhenReady = true
        }
    }

    override fun unbind(view: PlayerView) {
        view.player = null

        if (attachedViewRef?.get() === view) {
            attachedViewRef = null
            player?.pause()
        }
    }

    override fun pause() { player?.pause() }
    override fun resume() { player?.play() }

    override fun release() {
        player?.release()
        player = null
        currentUrl = null
        attachedViewRef?.get()?.player = null
        attachedViewRef = null
    }
}