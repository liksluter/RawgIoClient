package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.designsystem.theme.mediaOverlayScrim
import mr.liks.core.model.GameDetails
import mr.liks.core.ui.component.RatingBadge

/** Заголовок экрана деталей */
@Composable
fun DetailsHeader(
    details: GameDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MediaShape)
        ) {
            AsyncImage(
                model = details.backgroundImage,
                contentDescription = details.name,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, mediaOverlayScrim)
                        )
                    )
                    .align(androidx.compose.ui.Alignment.BottomCenter)
            )
        }

        Spacer(Modifier.height(RawgTheme.spacing.large))

        Text(
            text = details.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = RawgTheme.spacing.large)
        )

        Spacer(Modifier.height(RawgTheme.spacing.small))

        Row(
            modifier = Modifier.padding(horizontal = RawgTheme.spacing.large),
            horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            RatingBadge(rating = details.rating)
            details.metacritic?.let { score ->
                Text(
                    text = "Metacritic: $score",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            details.released?.let { date ->
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (details.developers.isNotEmpty()) {
            Spacer(Modifier.height(RawgTheme.spacing.medium))
            InfoRow(
                label = "Разработчик",
                value = details.developers.joinToString { it.name }
            )
        }

        if (details.publishers.isNotEmpty()) {
            Spacer(Modifier.height(RawgTheme.spacing.small))
            InfoRow(
                label = "Издатель",
                value = details.publishers.joinToString { it.name }
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = RawgTheme.spacing.large),
        horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}