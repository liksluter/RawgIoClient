package mr.liks.feature.search.impl.presentation

/** Эффекты экрана поиска */
sealed interface SearchEffect {

    /** Перейти на экран деталей игры с id [gameId] */
    data class NavigateToDetails(val gameId: Long) : SearchEffect

    /** Показать снекбар */
    data class ShowSnackbar(val message: String) : SearchEffect
}