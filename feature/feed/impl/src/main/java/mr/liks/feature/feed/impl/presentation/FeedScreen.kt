package mr.liks.feature.feed.impl.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.chrisbanes.haze.HazeState
import mr.liks.core.model.GamePreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun FeedScreen(
    onGameClick: (Long) -> Unit,
    hazeState: HazeState,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val games: LazyPagingItems<GamePreview> = viewModel.feed.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is FeedEffect.NavigateToDetails -> onGameClick(effect.gameId)
                is FeedEffect.ShowSnackbar -> Unit // TODO: snackbar host
            }
        }
    }

    FeedContent(
        games = games,
        uiState = uiState,
        hazeState = hazeState,
        listState = listState,
        contentPadding = contentPadding,
        onRefresh = { viewModel.onIntent(FeedIntent.Refresh) },
        onRetry = { viewModel.onIntent(FeedIntent.Retry) },
        onGameClick = { id -> viewModel.onIntent(FeedIntent.GameClicked(id)) },
        modifier = modifier.fillMaxSize()
    )
}