package mr.liks.core.network.api.dto

import kotlinx.serialization.Serializable

/**
 * Ответ со списком скриншотов
 *
 * @property count кол-во
 * @property results список скриншотов
 */
@Serializable
data class ScreenshotsResponse(
    val count: Int,
    val results: List<ScreenshotDto>
)
