package mr.liks.feature.search.impl.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GamePreview
import mr.liks.core.ui.component.RatingBadge
import mr.liks.feature.search.impl.R

@Composable
fun SearchResultItem(
    game: GamePreview,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = RawgTheme.spacing.large,
                vertical = RawgTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = game.backgroundImage,
            contentDescription = game.name,
            modifier = Modifier
                .size(width = 72.dp, height = 96.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = RawgTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBadge(
                    rating = game.rating,
                    modifier = Modifier.align(Alignment.Top)
                )
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            game.released?.let {
                Text(
                    text = "Релиз: $it",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            game.genres?.let {
                Text(
                    text = "Жанр: $it",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            game.platformsNames?.let {
                Text(
                    text = stringResource(R.string.platform_template, it),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}