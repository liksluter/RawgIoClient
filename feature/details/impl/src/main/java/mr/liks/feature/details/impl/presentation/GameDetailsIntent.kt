package mr.liks.feature.details.impl.presentation

/** Намерения на экране деталей */
sealed interface GameDetailsIntent {
    /** Первичная загрузка или pull-to-refresh */
    data object Refresh : GameDetailsIntent

    /** Открыть диалог просмотра медиа с индексом [index] */
    data class OpenMedia(val index: Int) : GameDetailsIntent

    /** Закрыть диалог просмотра медиа */
    data object CloseMedia : GameDetailsIntent

    /** Пользователь закрыл сообщение об ошибке */
    data object DismissError : GameDetailsIntent

    /** Retry после ошибки */
    data object Retry : GameDetailsIntent
}