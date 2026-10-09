package mr.liks.feature.details.impl.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import mr.liks.core.media.TrailerPlayer
import mr.liks.core.media.TrailerPlayerController
import mr.liks.core.model.GameMedia
import mr.liks.core.model.MediaItem
import mr.liks.feature.details.impl.R

/** Полноэкранный диалог просмотра медиа */
@Composable
fun MediaViewerDialog(
    media: GameMedia,
    initialIndex: Int,
    playerController: TrailerPlayerController,
    onDismiss: () -> Unit
) {
    val items = remember(media) { media.all }
    if (items.isEmpty()) {
        onDismiss()
        return
    }

    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, items.lastIndex),
        pageCount = { items.size }
    )

    LaunchedEffect(pagerState.currentPage, items) {
        val item = items.getOrNull(pagerState.currentPage)
        if (item is MediaItem.Trailer && item.value.playbackUrl != null) {
            playerController.resume()
        } else {
            playerController.pause()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val item = items[page]
                when (item) {
                    is MediaItem.Trailer -> TrailerPage(item, playerController)
                    is MediaItem.Screenshot -> ScreenshotPage(item)
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun TrailerPage(
    item: MediaItem.Trailer,
    playerController: TrailerPlayerController
) {
    val url = item.value.playbackUrl
    if (url == null) {
        ScreenshotPage(image = item.value.preview)
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TrailerPlayer(
            url = url,
            controller = playerController,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ScreenshotPage(item: MediaItem.Screenshot) {
    ScreenshotPage(image = item.value.image)
}

@Composable
private fun ScreenshotPage(image: String?) {
    val scale = remember { Animatable(1f) }
    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        coroutineScope.launch {
            val newScale = (scale.value * zoomChange).coerceIn(0.7f, 5f)
            scale.snapTo(newScale)

            if (newScale > 1f) {
                val newOffset = offset.value + offsetChange
                offset.snapTo(newOffset)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    translationX = offset.value.x,
                    translationY = offset.value.y
                )
                .transformable(
                    state = state,
                    canPan = { scale.value > 1f }
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.changes.all { !it.pressed }) {
                                coroutineScope.launch {
                                    launch { scale.animateTo(1f) }
                                    launch { offset.animateTo(Offset.Zero) }
                                }
                            }
                        }
                    }
                }
        )
    }
}