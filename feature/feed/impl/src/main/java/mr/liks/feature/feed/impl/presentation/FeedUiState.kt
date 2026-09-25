package mr.liks.feature.feed.impl.presentation

/**
 * Состояние экрана ленты
 *
 * @property isRefreshing `true` если выполняется обновление
 * @property errorMessage сообщение об ошибке
 */
data class FeedUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean get() = !isRefreshing && errorMessage == null
}