package mr.liks.feature.details.impl.presentation.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.common.ext.toRatingString
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
                    .align(Alignment.BottomCenter)
            )
        }

        Spacer(Modifier.height(RawgTheme.spacing.large))

        GameDescriptionWithRating(
            rating = details.rating,
            description = details.name,
            modifier = Modifier.padding(horizontal = RawgTheme.spacing.large)
        )

        Spacer(Modifier.height(RawgTheme.spacing.small))

        if (details.genres.isNotEmpty()) {
            Spacer(Modifier.height(RawgTheme.spacing.medium))
            InfoRow(
                label = "Жанр",
                value = details.genres.joinToString { it.name }
            )
        }

        if (details.platforms.isNotEmpty()) {
            Spacer(Modifier.height(RawgTheme.spacing.medium))
            InfoRow(
                label = "Платформы",
                value = details.platforms.joinToString { it.name }
            )
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

        if (!details.released.isNullOrEmpty()) {
            Spacer(Modifier.height(RawgTheme.spacing.small))
            InfoRow(
                label = "Дата релиза",
                value = details.released ?: ""
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

@Composable
fun GameDescriptionWithRating(
    rating: Double,
    description: String,
    modifier: Modifier = Modifier
) {
    if (rating <= 0.0) {
        Text(text = description, modifier = modifier)
        return
    }

    val badgeId = "rating_badge_id"
    val density = LocalDensity.current

    val textMeasurer = rememberTextMeasurer()
    val badgeText = remember(rating) { rating.toRatingString() }
    val badgeTextStyle = MaterialTheme.typography.labelMedium
    val horizontalSpacing = RawgTheme.spacing.small
    val verticalSpacing = RawgTheme.spacing.extraSmall

    val badgeWidthInSp = remember(badgeText, badgeTextStyle, density) {
        val textLayoutResult = textMeasurer.measure(text = badgeText, style = badgeTextStyle)

        with(density) {
            val horizontalPaddingPx = horizontalSpacing.toPx() * 2
            (textLayoutResult.size.width + horizontalPaddingPx).toSp()
        }
    }

    val badgeHeightInSp = remember(badgeText, badgeTextStyle, density) {
        val textLayoutResult = textMeasurer.measure(text = badgeText, style = badgeTextStyle)

        with(density) {
            val verticalPaddingPx = verticalSpacing.toPx() * 2
            (textLayoutResult.size.height + verticalPaddingPx).toSp()
        }
    }

    val annotatedText = buildAnnotatedString {
        appendInlineContent(id = badgeId, alternateText = "[$badgeText]")
        append(" ")
        append(description)
    }

    val inlineContent = mapOf(
        badgeId to InlineTextContent(
            placeholder = Placeholder(
                width = badgeWidthInSp,
                height = badgeHeightInSp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                RatingBadge(
                    rating = rating,
                    modifier = Modifier.wrapContentSize()
                )
            }
        }
    )

    Text(
        text = annotatedText,
        inlineContent = inlineContent,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        modifier = modifier
    )
}