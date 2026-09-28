package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GameMedia
import mr.liks.core.model.MediaItem

/** Лента превью медиа */
@Composable
fun MediaPager(
    media: GameMedia,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (media.isEmpty) return

    val items = media.all

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = RawgTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small)
    ) {
        itemsIndexed(
            items = items,
            key = { _, item ->
                when (item) {
                    is MediaItem.Trailer -> "t_${item.value.id}"
                    is MediaItem.Screenshot -> "s_${item.value.id}"
                }
            }
        ) { index, item ->
            MediaPreview(
                item = item,
                onClick = { onItemClick(index) }
            )
        }
    }
}

@Composable
private fun MediaPreview(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val aspectRatio = when (item) {
        is MediaItem.Trailer -> 16f / 9f
        is MediaItem.Screenshot -> item.value.aspectRatio
    }

    val imageUrl = when (item) {
        is MediaItem.Trailer -> item.value.preview
        is MediaItem.Screenshot -> item.value.image
    }

    Box(
        modifier = modifier
            .size(width = 220.dp, height = (220 / aspectRatio).dp)
            .clip(MediaShape)
            .background(Color.Black)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (item is MediaItem.Trailer) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0x66000000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Воспроизвести",
                    tint = Color.White
                )
            }
        }
    }
}