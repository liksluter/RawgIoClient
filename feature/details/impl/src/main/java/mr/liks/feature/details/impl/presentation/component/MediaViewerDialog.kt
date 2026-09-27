package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import mr.liks.core.media.TrailerPlayer
import mr.liks.core.model.GameMedia
import mr.liks.core.model.MediaItem

/** Полноэкранный диалог просмотра медиа */
@Composable
fun MediaViewerDialog(
    media: GameMedia,
    initialIndex: Int,
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
                    is MediaItem.Trailer -> TrailerPage(item)
                    is MediaItem.Screenshot -> ScreenshotPage(item)
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(40.dp)
                    .background(Color(0x66000000), shape = androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Закрыть",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun TrailerPage(item: MediaItem.Trailer) {
    val url = item.value.playbackUrl
    if (url == null) {
        ScreenshotPage(image = item.value.preview)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            TrailerPlayer(
                trailerUrl = url,
                autoPlay = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ScreenshotPage(item: MediaItem.Screenshot) {
    ScreenshotPage(image = item.value.image)
}

@Composable
private fun ScreenshotPage(image: String?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }
}