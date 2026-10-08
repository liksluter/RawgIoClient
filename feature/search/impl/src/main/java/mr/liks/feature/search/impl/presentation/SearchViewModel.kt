package mr.liks.feature.search.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mr.liks.core.common.StringProvider
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.model.GamePreview
import mr.liks.feature.search.impl.R
import mr.liks.feature.search.impl.domain.usecase.DeleteSearchHistoryItemUseCase
import mr.liks.feature.search.impl.domain.usecase.GetSearchHistoryUseCase
import mr.liks.feature.search.impl.domain.usecase.SaveSearchQueryUseCase
import mr.liks.feature.search.impl.domain.usecase.SearchGamesUseCase

/**
 * [ViewModel] экрана поиска
 *
 * @property searchGames usecase для поиска по запросу
 * @property getSearchHistory usecase для получения flow истории поиска
 * @property saveSearchQuery usecase для сохранения запроса в историю
 * @property deleteSearchHistoryItem usecase для удаления запроса из истории
 * @property stringProvider провайдер строк
 * @property logger логер
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val searchGames: SearchGamesUseCase,
    private val getSearchHistory: GetSearchHistoryUseCase,
    private val saveSearchQuery: SaveSearchQueryUseCase,
    private val deleteSearchHistoryItem: DeleteSearchHistoryItemUseCase,
    private val stringProvider: StringProvider,
    private val logger: AppLogger
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _effects = Channel<SearchEffect>(capacity = Channel.BUFFERED)
    val effects: Flow<SearchEffect> = _effects.receiveAsFlow()

    val results: Flow<PagingData<GamePreview>> = _uiState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(DEBOUNCE_MS)
        .flatMapLatest { query ->
            searchGames(query)
        }
        .cachedIn(viewModelScope)

    init {
        observeHistory()
    }

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.QueryChanged -> onQueryChanged(intent.query)
            SearchIntent.Submit -> onSubmit()
            is SearchIntent.HistoryItemClicked -> onSubmitHistory(intent.query)
            is SearchIntent.DeleteHistoryItem -> onDeleteHistory(intent.query)
            SearchIntent.ClearQuery -> onQueryChanged("")
            is SearchIntent.GameClicked -> onGameClicked(intent.gameId)
            SearchIntent.DismissError ->
                _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun observeHistory() {
        getSearchHistory()
            .onEach { history ->
                _uiState.update { it.copy(history = history) }
            }
            .launchIn(viewModelScope)
    }

    private fun onQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                query = query,
                showHistory = query.isBlank()
            )
        }
    }

    private fun onSubmit() {
        val query = _uiState.value.query
        if (query.isBlank()) return
        persistQuery(query)
    }

    private fun onSubmitHistory(query: String) {
        _uiState.update { it.copy(query = query, showHistory = false) }
        persistQuery(query)
    }

    private fun persistQuery(query: String) {
        viewModelScope.launch {
            runCatching { saveSearchQuery(query) }
                .onFailure { t ->
                    logger.w(t, "Failed to save search query")
                }
        }
    }

    private fun onDeleteHistory(query: String) {
        viewModelScope.launch {
            runCatching { deleteSearchHistoryItem(query) }
                .onFailure { t ->
                    logger.w(t, "Failed to delete search history item")
                    _uiState.update {
                        it.copy(errorMessage = t.message
                            ?: stringProvider.getString(R.string.delete_search_record_error))
                    }
                }
        }
    }

    private fun onGameClicked(gameId: Long) {
        viewModelScope.launch {
            _effects.send(SearchEffect.NavigateToDetails(gameId))
        }
    }

    private companion object {
        const val DEBOUNCE_MS = 300L
    }
}