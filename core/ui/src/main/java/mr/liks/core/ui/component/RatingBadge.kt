package mr.liks.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import mr.liks.core.common.ext.toRatingString
import mr.liks.core.designsystem.theme.RatingShape
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.designsystem.theme.ratingAverage
import mr.liks.core.designsystem.theme.ratingBad
import mr.liks.core.designsystem.theme.ratingGood

/**
 * Бейдж с рейтингом
 *
 * @param rating рейтинг от 0.0 до 5.0
 * @param modifier модификатор
 */
@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier
) {
    if (rating <= 0.0) return

    val color = ratingColor(rating)

    Surface(
        modifier = modifier,
        color = color,
        shape = RatingShape
    ) {
        Text(
            text = rating.toRatingString(),
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = RawgTheme.spacing.small,
                vertical = RawgTheme.spacing.extraSmall
            )
        )
    }
}

private fun ratingColor(rating: Double): Color = when {
    rating >= 4.0 -> ratingGood
    rating >= 3.0 -> ratingAverage
    else -> ratingBad
}

@Preview(showBackground = true)
@Composable
private fun RatingBadgePreview() {
    RatingBadge(rating = 4.5)
}