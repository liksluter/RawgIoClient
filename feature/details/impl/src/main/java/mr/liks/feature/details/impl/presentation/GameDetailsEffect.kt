package mr.liks.feature.details.impl.presentation

/** Эффекты экрана деталей */
sealed interface GameDetailsEffect {
    /** Вернуться назад */
    data object NavigateBack : GameDetailsEffect

    /** Показать снекбар */
    data class ShowSnackbar(val message: String) : GameDetailsEffect
}