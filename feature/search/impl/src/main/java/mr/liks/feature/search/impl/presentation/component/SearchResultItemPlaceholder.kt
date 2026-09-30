package mr.liks.feature.search.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.ui.effect.FeedShimmer
import mr.liks.core.ui.effect.shimmer

@Composable
fun SearchResultItemPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = RawgTheme.spacing.large,
                vertical = RawgTheme.spacing.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 72.dp, height = 96.dp)
                .shimmer(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = RawgTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(18.dp)
                    .shimmer(RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(18.dp)
                    .shimmer(RoundedCornerShape(4.dp))
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 44.dp, height = 18.dp)
                        .shimmer(RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .size(width = 120.dp, height = 14.dp)
                        .shimmer(RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun SearchResultPlaceholderList(
    modifier: Modifier = Modifier,
    itemCount: Int = 8
) {
    FeedShimmer {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = RawgTheme.spacing.small,
                bottom = RawgTheme.spacing.huge
            ),
            verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
        ) {
            items(itemCount) {
                SearchResultItemPlaceholder()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultItemPlaceholderPreview() {
    FeedShimmer {
        SearchResultItemPlaceholder()
    }
}