package mr.liks.core.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import mr.liks.core.designsystem.theme.CardShape
import mr.liks.core.designsystem.theme.MediaShape
import mr.liks.core.designsystem.theme.RawgTheme

/**
 * Плейсхолдер ленты на время первой загрузки
 *
 * @param modifier можификатор
 * @param itemCount кол-во элементов
 */
@Composable
fun FeedPlaceholder(
    modifier: Modifier = Modifier,
    topPadding: Dp,
    itemCount: Int = DEFAULT_ITEM_COUNT
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = topPadding + RawgTheme.spacing.feedCardVertical,
            bottom = RawgTheme.spacing.huge
        ),
        verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.feedCardVertical),
        userScrollEnabled = false
    ) {
        items(count = itemCount, key = { it }) {
            FeedPlaceholderCard()
        }
    }
}

@Composable
private fun FeedPlaceholderCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = RawgTheme.spacing.feedCardHorizontal),
        shape = CardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = RawgTheme.elevation.card)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MediaShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(Modifier.height(RawgTheme.spacing.small))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = RawgTheme.spacing.cardInner),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 20.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Spacer(Modifier.height(RawgTheme.spacing.small))

            Box(
                modifier = Modifier
                    .padding(horizontal = RawgTheme.spacing.cardInner)
                    .fillMaxWidth(0.6f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(Modifier.height(RawgTheme.spacing.small))
        }
    }
}

private const val DEFAULT_ITEM_COUNT = 3

@Preview(showBackground = true)
@Composable
private fun FeedPlaceholderPreview() {
    FeedPlaceholder(topPadding = 0.dp)
}