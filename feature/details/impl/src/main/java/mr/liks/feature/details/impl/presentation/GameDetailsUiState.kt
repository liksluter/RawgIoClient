package mr.liks.feature.details.impl.presentation

import mr.liks.core.model.GameDetails
import mr.liks.core.model.GameMedia

/**
 * Состояние экрана деталей
 *
 * @property details основная информация, null — ещё не загружена
 * @property media медиа, пустой [GameMedia] — либо ещё не загружено, либо у игры нет медиа
 * @property isLoadingDetails первичная загрузка деталей
 * @property isLoadingMedia первичная загрузка медиа
 * @property errorMessage одноразовая ошибка поверх контента
 * @property selectedMediaIndex индекс открытого в диалоге медиа, null — диалог закрыт
 */
data class GameDetailsUiState(
    val details: GameDetails? = null,
    val media: GameMedia = GameMedia(trailers = emptyList(), screenshots = emptyList()),
    val isLoadingDetails: Boolean = true,
    val isLoadingMedia: Boolean = true,
    val errorMessage: String? = null,
    val selectedMediaIndex: Int? = null
) {
    /** Показывать ли шиммеры — нет ни данных, ни загрузки */
    val isInitialLoading: Boolean
        get() = isLoadingDetails && details == null
}
