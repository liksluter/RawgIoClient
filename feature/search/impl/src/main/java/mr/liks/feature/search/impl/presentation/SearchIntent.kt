package mr.liks.feature.search.impl.presentation

/** Намерения на экране поиска */
sealed interface SearchIntent {
    /** Пользователь изменил текст в поле ввода */
    data class QueryChanged(val query: String) : SearchIntent

    /** Пользователь нажал "Поиск" */
    data object Submit : SearchIntent

    /** Пользователь тапнул по элементу истории */
    data class HistoryItemClicked(val query: String) : SearchIntent

    /** Пользователь удалил элемент истории */
    data class DeleteHistoryItem(val query: String) : SearchIntent

    /** Пользователь очистил всё поле ввода */
    data object ClearQuery : SearchIntent

    /** Пользователь тапнул по результату поиска */
    data class GameClicked(val gameId: Long) : SearchIntent

    /** Закрыть сообщение об ошибке */
    data object DismissError : SearchIntent
}