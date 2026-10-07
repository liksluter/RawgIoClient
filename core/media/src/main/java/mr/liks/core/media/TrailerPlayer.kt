package mr.liks.core.media

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun TrailerPlayer(
    url: String,
    controller: TrailerPlayerController,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(
            factory = { ctx -> PlayerView(ctx) },
            update = { view ->
                controller.bindTo(view, url)
            },
            onRelease = { view ->
                view.player = null
                controller.unbind()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}