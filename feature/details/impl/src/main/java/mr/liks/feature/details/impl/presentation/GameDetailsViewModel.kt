package mr.liks.feature.details.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mr.liks.core.common.StringProvider
import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.media.TrailerPlayerController
import mr.liks.feature.details.impl.R
import mr.liks.feature.details.impl.domain.usecase.GetGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.GetGameMediaUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameDetailsUseCase
import mr.liks.feature.details.impl.domain.usecase.RefreshGameMediaUseCase

/**
 * ViewModel экрана деталей игры
 *
 * @property getGameDetails usecase потока деталей игры из БД
 * @property getGameMedia usecase потока медиа из БД
 * @property refreshDetails usecase обновления деталей игры из сети
 * @property refreshMedia usecase обновления медиа из сети
 * @property trailerPlayerController контроллер для видеопроигрывателя
 * @property logger логер
 */
class GameDetailsViewModel(
    private val getGameDetails: GetGameDetailsUseCase,
    private val getGameMedia: GetGameMediaUseCase,
    private val refreshDetails: RefreshGameDetailsUseCase,
    private val refreshMedia: RefreshGameMediaUseCase,
    private val stringProvider: StringProvider,
    val trailerPlayerController: TrailerPlayerController,
    private val logger: AppLogger,
) : ViewModel() {
    private var gameId: Long = 0
    private var detailsJob: Job? = null
    private var mediaJob: Job? = null

    private val _uiState = MutableStateFlow(GameDetailsUiState())
    /** Состояние экрана */
    val uiState: StateFlow<GameDetailsUiState> = _uiState.asStateFlow()

    private val _effects = Channel<GameDetailsEffect>(capacity = Channel.BUFFERED)
    /** Эффекты экрана */
    val effects: Flow<GameDetailsEffect> = _effects.receiveAsFlow()

    /** Загружает детали и медия игры с идентификатором [gameId] */
    fun loadGameDetails(gameId: Long) {
        if (gameId == this.gameId && _uiState.value.details != null) return

        _uiState.value = GameDetailsUiState()
        this.gameId = gameId
        observeDetails()
        observeMedia()
        refresh(force = false)
    }

    /** Обрабатывает интенты с экрана */
    fun onIntent(intent: GameDetailsIntent) {
        when (intent) {
            GameDetailsIntent.Refresh -> refresh(force = true)
            GameDetailsIntent.Retry -> refresh(force = true)
            GameDetailsIntent.DismissError ->
                _uiState.update { it.copy(errorMessage = null) }
            is GameDetailsIntent.OpenMedia ->
                _uiState.update { it.copy(selectedMediaIndex = intent.index) }
            GameDetailsIntent.CloseMedia ->
                _uiState.update {
                    trailerPlayerController.pause()
                    it.copy(selectedMediaIndex = null)
                }
        }
    }

    override fun onCleared() {
        trailerPlayerController.release()
        super.onCleared()
    }

    private fun observeDetails() {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch {
            getGameDetails(gameId).collect { details ->
                if (details == null && _uiState.value.isInitialLoading) return@collect
                _uiState.update {
                    it.copy(
                        details = details,
                        isLoadingDetails = false
                    )
                }
            }
        }
    }

    private fun observeMedia() {
        mediaJob?.cancel()
        mediaJob = viewModelScope.launch {
            getGameMedia(gameId).collect { media ->
                if (media.isEmpty && _uiState.value.isInitialLoading) return@collect
                _uiState.update {
                    it.copy(
                        media = media,
                        isLoadingMedia = false
                    )
                }
            }
        }
    }

    private fun refresh(force: Boolean) {
        viewModelScope.launch {
            val state = _uiState.value
            val needsDetails = force || state.details == null
            val needsMedia = force || state.media.isEmpty

            if (needsDetails) {
                _uiState.update { it.copy(isLoadingDetails = true, errorMessage = null) }
                runCatching { refreshDetails(gameId) }
                    .onFailure { t ->
                        logger.e(t, "Failed to refresh details for %d", gameId)
                        _uiState.update {
                            it.copy(
                                isLoadingDetails = false,
                                errorMessage = t.message
                                    ?: stringProvider.getString(R.string.details_load_error_message)
                            )
                        }
                        _effects.send(
                            GameDetailsEffect.ShowSnackbar(
                                t.message ?: stringProvider.getString(R.string.details_load_error_message)
                            )
                        )
                    }
            }

            if (needsMedia) {
                _uiState.update { it.copy(isLoadingMedia = true) }
                runCatching { refreshMedia(gameId) }
                    .onFailure { t ->
                        logger.w(t, "Failed to refresh media for %d", gameId)
                        _uiState.update { it.copy(isLoadingMedia = false) }
                    }
            }
        }
    }
}