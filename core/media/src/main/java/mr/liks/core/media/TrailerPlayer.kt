package mr.liks.core.media

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun TrailerPlayer(
    url: String,
    isActive: Boolean,
    controller: TrailerPlayerController,
    modifier: Modifier = Modifier
) {
    var wasActive by remember { mutableStateOf(false) }

    Box(modifier = modifier.background(Color.Black).padding(32.dp)) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = true
                    controllerAutoShow = false
                    controllerHideOnTouch = true
                    controllerShowTimeoutMs = 1500
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    setShowFastForwardButton(true)
                    setShowRewindButton(true)
                }
            },
            update = { view ->
                if (isActive) {
                    controller.bindTo(view, url)
                    if (!wasActive) {
                        wasActive = true
                        view.post { view.hideController() }
                    }
                } else {
                    wasActive = false
                    controller.unbind(view)
                }
            },
            onRelease = { view ->
                view.player = null
                controller.unbind(view)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}