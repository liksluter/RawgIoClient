package mr.liks.core.media

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun TrailerPlayer(
    trailerUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true
) {
    val context = LocalContext.current

    val player: ExoPlayer = remember {
        ExoPlayerFactory.createDetailPlayer(context).apply {
            setMediaItem(MediaItem.fromUri(trailerUrl))
            prepare()
            playWhenReady = autoPlay
        }
    }

    LaunchedEffect(trailerUrl) {
        player.setMediaItem(MediaItem.fromUri(trailerUrl))
        player.prepare()
        player.playWhenReady = autoPlay
    }

    DisposableEffect(Unit) {
        onDispose {
            player.stop()
            player.release()
        }
    }

    Box(
        modifier = modifier.background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = true
                    this.player = player
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(true)
                    setShowRewindButton(true)
                }
            },
            update = { view ->
                view.player = player
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}