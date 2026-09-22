package mr.liks.feature.feed.impl.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.designsystem.theme.CardShape
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.media.TrailerPreview
import mr.liks.core.model.GamePreview
import mr.liks.core.ui.component.RatingBadge

/** Карточка игры в ленте */
@Composable
fun GameFeedCard(
    game: GamePreview,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RawgTheme.spacing.feedCardHorizontal)
            .clickable(onClick = onClick),
        shape = CardShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = RawgTheme.elevation.card
        )
    ) {
        Column {
            MediaBlock(
                game = game,
                isVisible = isVisible
            )

            Spacer(Modifier.height(RawgTheme.spacing.small))

            MetaRow(game = game)

            Spacer(Modifier.height(RawgTheme.spacing.small))

            Text(
                text = "${game.feedOrder} ${game.name}", // todo для тестов, убрать после стабилизации
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    horizontal = RawgTheme.spacing.cardInner,
                    vertical = RawgTheme.spacing.extraSmall
                )
            )

            Spacer(Modifier.height(RawgTheme.spacing.small))
        }
    }
}

@Composable
private fun MediaBlock(
    game: GamePreview,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(MediaShape)
    ) {
        val trailerUrl = game.trailerUrl
        if (trailerUrl != null) {
            TrailerPreview(
                trailerUrl = trailerUrl,
                isVisible = isVisible,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            AsyncImage(
                model = game.backgroundImage,
                contentDescription = game.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun MetaRow(
    game: GamePreview,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RawgTheme.spacing.cardInner),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
        ) {
            game.platformsNames?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.padding(
                        horizontal = RawgTheme.spacing.small,
                        vertical = RawgTheme.spacing.extraSmall
                    ),
                )
            } ?: run {
                game.platforms.forEach { platform ->
                    AsyncImage(
                        model = platform.iconUrl,
                        contentDescription = platform.name,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(Modifier.size(RawgTheme.spacing.small))

        RatingBadge(rating = game.rating)
    }
}