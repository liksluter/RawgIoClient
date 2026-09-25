package mr.liks.feature.feed.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GamePreview
import mr.liks.core.ui.component.ErrorFooter
import mr.liks.core.ui.component.FeedEmpty
import mr.liks.core.ui.component.FeedError
import mr.liks.core.ui.component.FeedPlaceholder
import mr.liks.core.ui.component.LoadingFooter
import mr.liks.feature.feed.impl.presentation.component.GameFeedCard

@Composable
internal fun FeedContent(
    games: LazyPagingItems<GamePreview>,
    uiState: FeedUiState,
    hazeState: HazeState,
    listState: LazyListState,
    contentPadding: PaddingValues,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .hazeSource(hazeState)
    ) {
        val firstVisibleIndex by remember {
            derivedStateOf { listState.firstVisibleItemIndex }
        }

        when (val refresh = games.loadState.refresh) {
            is LoadState.Loading -> {
                FeedPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    topPadding = contentPadding.calculateTopPadding()
                )
            }

            is LoadState.Error -> {
                FeedError(
                    message = refresh.error.message ?: "Не удалось загрузить ленту",
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is LoadState.NotLoading -> {
                if (games.itemCount == 0) {
                    FeedEmpty(modifier = Modifier.fillMaxSize())
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = contentPadding.calculateTopPadding() +
                                    RawgTheme.spacing.feedCardVertical,
                            bottom = contentPadding.calculateBottomPadding() +
                                    RawgTheme.spacing.huge
                        ),
                        verticalArrangement = Arrangement.spacedBy(
                            RawgTheme.spacing.feedCardVertical
                        )
                    ) {
                        items(
                            count = games.itemCount,
                            key = games.itemKey { it.id }
                        ) { index ->
                            val game = games[index] ?: return@items
                            GameFeedCard(
                                game = game,
                                isVisible = index == firstVisibleIndex,
                                onClick = { onGameClick(game.id) }
                            )
                        }

                        when (val append = games.loadState.append) {
                            is LoadState.Loading -> item { LoadingFooter() }
                            is LoadState.Error -> item {
                                ErrorFooter(
                                    onRetry = { games.retry() },
                                    message = append.error.message
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }

        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(RawgTheme.spacing.large)
                )
            }
        }
    }
}