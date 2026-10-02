package mr.liks.feature.search.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.GamePreview
import mr.liks.core.model.SearchHistoryItem
import mr.liks.core.ui.component.ErrorFooter
import mr.liks.core.ui.component.FeedEmpty
import mr.liks.core.ui.component.FeedError
import mr.liks.core.ui.component.LoadingFooter
import mr.liks.feature.search.impl.presentation.component.SearchHistoryRow
import mr.liks.feature.search.impl.presentation.component.SearchResultItem
import mr.liks.feature.search.impl.presentation.component.SearchResultPlaceholderList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    onGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val results = viewModel.results.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SearchEffect.NavigateToDetails -> onGameClick(effect.gameId)
                is SearchEffect.ShowSnackbar -> Unit
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SearchField(
            query = uiState.query,
            onQueryChange = { viewModel.onIntent(SearchIntent.QueryChanged(it)) },
            onSubmit = { viewModel.onIntent(SearchIntent.Submit) },
            onClear = { viewModel.onIntent(SearchIntent.ClearQuery) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.showHistory) {
                HistoryContent(
                    history = uiState.history,
                    onItemClick = { viewModel.onIntent(SearchIntent.HistoryItemClicked(it)) },
                    onDeleteClick = { viewModel.onIntent(SearchIntent.DeleteHistoryItem(it)) }
                )
            } else {
                ResultsContent(
                    results = results,
                    onItemClick = { viewModel.onIntent(SearchIntent.GameClicked(it)) }
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboard = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = RawgTheme.spacing.large,
                vertical = RawgTheme.spacing.small
            ),
        placeholder = { Text("Поиск игр") },
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Очистить"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboard?.hide()
                onSubmit()
            }
        )
    )
}

@Composable
private fun HistoryContent(
    history: List<SearchHistoryItem>,
    onItemClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    ClearFocus(lazyListState.isScrollInProgress)

    if (history.isEmpty()) {
        FeedEmpty(
            modifier = modifier.fillMaxSize(),
            message = "Введите запрос, чтобы найти игру"
        )
        return
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = history.size,
            key = { index -> history[index].id }
        ) { index ->
            val item = history[index]
            SearchHistoryRow(
                query = item.query,
                onClick = { onItemClick(item.query) },
                onDelete = { onDeleteClick(item.query) }
            )
        }
    }
}

@Composable
private fun ResultsContent(
    results: androidx.paging.compose.LazyPagingItems<GamePreview>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    ClearFocus(lazyListState.isScrollInProgress)

    when (val refresh = results.loadState.refresh) {
        is LoadState.Loading -> {
            SearchResultPlaceholderList(modifier)
        }

        is LoadState.Error -> {
            FeedError(
                message = refresh.error.message ?: "Не удалось выполнить поиск",
                onRetry = { results.retry() },
                modifier = modifier.fillMaxSize()
            )
        }

        is LoadState.NotLoading -> {
            if (results.itemCount == 0) {
                FeedEmpty(
                    modifier = modifier.fillMaxSize(),
                    message = "Ничего не найдено"
                )
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = RawgTheme.spacing.small,
                        bottom = RawgTheme.spacing.huge
                    ),
                    verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
                ) {
                    items(
                        count = results.itemCount,
                        key = results.itemKey { it.id }
                    ) { index ->
                        results[index]?.let { game ->
                            SearchResultItem(
                                game = game,
                                onClick = { onItemClick(game.id) }
                            )
                        }
                    }

                    when (val append = results.loadState.append) {
                        is LoadState.Loading -> item { LoadingFooter() }
                        is LoadState.Error -> item {
                            ErrorFooter(
                                onRetry = { results.retry() },
                                message = append.error.message
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun ClearFocus(isScrollInProgress: Boolean) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isScrollInProgress) {
        if (isScrollInProgress) {
            focusManager.clearFocus()
        }
    }
}