package mr.liks.feature.feed.impl.presentation

/** Намерения пользователя на экране ленты */
sealed interface FeedIntent {

    /** Обновить ленту */
    data object Refresh : FeedIntent

    /** Тап по карточке игры */
    data class GameClicked(val gameId: Long) : FeedIntent

    /** Пользователь закрыл сообщение об ошибке */
    data object DismissError : FeedIntent

    /** Retry после ошибки загрузки следующей страницы */
    data object Retry : FeedIntent
}
