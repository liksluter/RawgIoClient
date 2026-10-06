package mr.liks.feature.feed.impl.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import mr.liks.core.designsystem.theme.CardShape
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.media.TrailerPreview
import mr.liks.core.model.GamePreview
import mr.liks.core.ui.component.RatingBadge
import mr.liks.core.ui.effect.shimmer

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

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = RawgTheme.spacing.cardInner),
            ) {
                game.genres.takeIf { !it.isNullOrEmpty() }?.let {
                    Text(
                        text = "Жанр: $it",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Thin,
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(
                                horizontal = RawgTheme.spacing.small,
                                vertical = RawgTheme.spacing.extraSmall
                            ),
                    )
                }
                game.platformsNames.takeIf { !it.isNullOrEmpty() }?.let {
                    Text(
                        text = "Платформы: $it",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Thin,
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(
                                horizontal = RawgTheme.spacing.small,
                                vertical = RawgTheme.spacing.extraSmall
                            ),
                    )
                }
            }

            Spacer(Modifier.height(RawgTheme.spacing.small))

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = RawgTheme.spacing.cardInner,
                        vertical = RawgTheme.spacing.extraSmall
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                RatingBadge(rating = game.rating)
            }

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
            val context = LocalContext.current
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(game.backgroundImage)
                    .crossfade(true)
                    .build()
            )

            val state by painter.state.collectAsState()

            val isLoading = state is AsyncImagePainter.State.Loading
            val isError = state is AsyncImagePainter.State.Error

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            } else if (isError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Image(
                painter = painter,
                contentDescription = game.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}