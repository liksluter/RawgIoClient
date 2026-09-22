package mr.liks.core.model

/**
 * Трейлер игры
 *
 * @property id шв
 * @property name название
 * @property preview URL превью
 * @property data480 URL видео в качестве 480p
 * @property dataMax URL видео в максимальном качестве
 */
data class Trailer(
    val id: Long,
    val name: String,
    val preview: String?,
    val data480: String?,
    val dataMax: String?
) {
    /** URL для воспроизведения */
    val playbackUrl: String? get() = dataMax ?: data480
}