package mr.liks.feature.feed.impl.presentation

/** Одноразовые эффекты */
sealed interface FeedEffect {

    /** Перейти на экран деталей игры [gameId] */
    data class NavigateToDetails(val gameId: Long) : FeedEffect

    /** Показать снекбар с текстом [message] */
    data class ShowSnackbar(val message: String) : FeedEffect
}