package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.ui.effect.FeedShimmer
import mr.liks.core.ui.effect.shimmer

/** Плейсхолдер экрана деталей игры на время первой загрузки */
@Composable
fun GameDetailsPlaceholder(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    FeedShimmer {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.large),
            userScrollEnabled = false
        ) {
            item("header") { HeaderPlaceholder() }
            item("media") { MediaRowPlaceholder() }
            item("description") { DescriptionPlaceholder() }
        }
    }
}

@Composable
private fun HeaderPlaceholder(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MediaShape)
                .shimmer()
        )

        Spacer(Modifier.height(RawgTheme.spacing.large))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = RawgTheme.spacing.large),
            horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .shimmer()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )
        }

        Spacer(Modifier.height(RawgTheme.spacing.extraSmall))

        Box(
            modifier = Modifier
                .padding(horizontal = RawgTheme.spacing.large)
                .fillMaxWidth(0.5f)
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )

        repeat(INFO_ROWS) {
            Spacer(Modifier.height(RawgTheme.spacing.medium))
            InfoRowPlaceholder()
        }
    }
}

@Composable
private fun InfoRowPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RawgTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )
    }
}

@Composable
private fun MediaRowPlaceholder(modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = RawgTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small),
        userScrollEnabled = false
    ) {
        items(count = MEDIA_ITEMS, key = { it }) {
            Box(
                modifier = Modifier
                    .size(width = MEDIA_ITEM_WIDTH, height = MEDIA_ITEM_HEIGHT)
                    .clip(MediaShape)
                    .shimmer()
            )
        }
    }
}

@Composable
private fun DescriptionPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RawgTheme.spacing.large)
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
        )

        Spacer(Modifier.height(RawgTheme.spacing.small))

        repeat(DESCRIPTION_LINES) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (index == DESCRIPTION_LINES - 1) 0.6f else 1f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmer()
            )
            if (index < DESCRIPTION_LINES - 1) {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

private const val INFO_ROWS = 4
private const val MEDIA_ITEMS = 3
private const val DESCRIPTION_LINES = 3

private val MEDIA_ITEM_WIDTH = 220.dp
private val MEDIA_ITEM_HEIGHT = (220 / (16f / 9f)).dp