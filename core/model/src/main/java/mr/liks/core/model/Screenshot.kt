package mr.liks.core.model

/**
 * Скриншот игры
 *
 * @property id шв
 * @property image URL изображения
 * @property width ширина в пикселях
 * @property height высота в пикселях
 */
data class Screenshot(
    val id: Long,
    val image: String,
    val width: Int,
    val height: Int
) {
    /** Отношение ширины к высоте */
    val aspectRatio: Float
        get() = if (width > 0 && height > 0) width.toFloat() / height.toFloat() else 16f / 9f
}
