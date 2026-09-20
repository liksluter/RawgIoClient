package mr.liks.core.media

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun TrailerPreview(
    trailerUrl: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val context = LocalContext.current

    val player: ExoPlayer = remember {
        ExoPlayerFactory.createFeedPlayer(context).apply {
            setMediaItem(MediaItem.fromUri(trailerUrl))
            prepare()
        }
    }

    LaunchedEffect(trailerUrl) {
        player.setMediaItem(MediaItem.fromUri(trailerUrl))
        player.prepare()
        if (isVisible) player.play()
    }

    LaunchedEffect(isVisible) {
        if (isVisible) player.play() else player.pause()
    }

    DisposableEffect(Unit) {
        onDispose {
            player.stop()
            player.release()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                    this.player = player
                }
            },
            update = { view ->
                view.player = player
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun TrailerPreviewAutoPlay(
    trailerUrl: String,
    modifier: Modifier = Modifier
) {
    TrailerPreview(
        trailerUrl = trailerUrl,
        isVisible = true,
        modifier = modifier
    )
}

/** Останавливает плеер, если он в состоянии playing */
fun ExoPlayer.pauseIfPlaying() {
    if (playbackState == Player.STATE_READY && isPlaying) pause()
}