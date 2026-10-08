package mr.liks.feature.search.impl.presentation

import mr.liks.core.model.SearchHistoryItem

/**
 * Состояние экрана поиска
 *
 * @property query текущий запрос
 * @property history история поиска
 * @property showHistory показывать ли историю
 * @property errorMessage ошибка
 */
data class SearchUiState(
    val query: String = "",
    val history: List<SearchHistoryItem> = emptyList(),
    val showHistory: Boolean = true,
    val errorMessage: String? = null
)