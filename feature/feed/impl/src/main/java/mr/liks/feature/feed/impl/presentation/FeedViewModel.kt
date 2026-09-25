package mr.liks.feature.feed.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.model.GamePreview
import mr.liks.feature.feed.impl.domain.usecase.GetFeedPagingDataUseCase
import mr.liks.feature.feed.impl.domain.usecase.RefreshFeedUseCase

/**
 * ViewModel экрана ленты
 *
 * @property refreshFeed usecase обновления ленты
 * @property logger логгер
 * @param getFeedPagingData usecase потока страниц ленты
 */
class FeedViewModel(
    getFeedPagingData: GetFeedPagingDataUseCase,
    private val refreshFeed: RefreshFeedUseCase,
    private val logger: AppLogger
) : ViewModel() {
    val feed: Flow<PagingData<GamePreview>> = getFeedPagingData()
        .cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _effects = Channel<FeedEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<FeedEffect> = _effects.receiveAsFlow()

    fun onIntent(intent: FeedIntent) {
        when (intent) {
            FeedIntent.Refresh -> onRefresh()
            FeedIntent.Retry -> onRefresh()
            FeedIntent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            is FeedIntent.GameClicked -> onGameClicked(intent.gameId)
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            runCatching { refreshFeed() }
                .onSuccess {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
                .onFailure { throwable ->
                    logger.e(throwable, "Feed refresh failed")
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            errorMessage = throwable.message ?: "Не удалось обновить ленту"
                        )
                    }
                }
        }
    }

    private fun onGameClicked(gameId: Long) {
        viewModelScope.launch {
            _effects.send(FeedEffect.NavigateToDetails(gameId))
        }
    }
}