package mr.liks.feature.details.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.ui.component.FeedError
import mr.liks.core.ui.component.FeedPlaceholder
import mr.liks.feature.details.impl.presentation.component.DetailsHeader
import mr.liks.feature.details.impl.presentation.component.MediaPager
import mr.liks.feature.details.impl.presentation.component.MediaViewerDialog
import org.koin.androidx.compose.koinViewModel

/** Экран деталей игры */
@Composable
fun GameDetailsScreen(
    gameId: Long,
    contentPadding: PaddingValues,
    onBack: () -> Unit,
    viewModel: GameDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                GameDetailsEffect.NavigateBack -> onBack()
                is GameDetailsEffect.ShowSnackbar -> Unit // todo
            }
        }
    }

    LaunchedEffect(gameId) {
        viewModel.loadGameDetails(gameId)
    }

    when {
        uiState.isInitialLoading -> {
            FeedPlaceholder(modifier = Modifier.fillMaxSize(), topPadding = 0.dp)
        }

        uiState.details == null -> {
            FeedError(
                message = uiState.errorMessage ?: "Не удалось загрузить игру",
                onRetry = { viewModel.onIntent(GameDetailsIntent.Retry) },
                modifier = Modifier.fillMaxSize()
            )
        }

        else -> {
            DetailsContent(
                uiState = uiState,
                onMediaClick = { index ->
                    viewModel.onIntent(GameDetailsIntent.OpenMedia(index))
                },
                contentPadding = contentPadding
            )

            uiState.selectedMediaIndex?.let { index ->
                MediaViewerDialog(
                    media = uiState.media,
                    initialIndex = index,
                    onDismiss = { viewModel.onIntent(GameDetailsIntent.CloseMedia) }
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    uiState: GameDetailsUiState,
    contentPadding: PaddingValues,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = uiState.details ?: return

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.large)
    ) {
        item("header") {
            DetailsHeader(details = details)
        }

        if (uiState.media.all.isNotEmpty()) {
            item("media") {
                MediaPager(
                    media = uiState.media,
                    onItemClick = onMediaClick
                )
            }
        }

        val description = details.descriptionRaw ?: details.description
        if (!description.isNullOrBlank()) {
            item("description") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = RawgTheme.spacing.large)
                ) {
                    Text(
                        text = "Об игре",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(RawgTheme.spacing.small))
                    Text(
                        text = AnnotatedString.fromHtml(htmlString = description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}