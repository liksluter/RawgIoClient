package mr.liks.core.model

/**
 * Медиа-контент: трейлеры и скриншоты
 *
 * @property trailers список трейлеров
 * @property screenshots список скриншотов
 */
data class GameMedia(
    val trailers: List<Trailer>,
    val screenshots: List<Screenshot>
) {
    /** @retutn `true`, если у игры нет ни трейлеров, ни скриншотов. */
    val isEmpty: Boolean get() = trailers.isEmpty() && screenshots.isEmpty()

    /**
     * Единый список превью для горизонтальной ленты: сначала трейлеры,
     * потом скриншоты
     */
    val all: List<MediaItem>
        get() = buildList {
            addAll(trailers.map { MediaItem.Trailer(it) })
            addAll(screenshots.map { MediaItem.Screenshot(it) })
        }
}

/** Обёртка над трейлером или скриншотом для отображения в общем списке */
sealed interface MediaItem {
    data class Trailer(val value: mr.liks.core.model.Trailer) : MediaItem
    data class Screenshot(val value: mr.liks.core.model.Screenshot) : MediaItem
}
