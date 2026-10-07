package mr.liks.core.media

import androidx.compose.runtime.Stable
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/** Контракт для управления [ExoPlayer] для экрана деталей */
@Stable
interface TrailerPlayerController {

    /** Привязывает плеер для [url] к переданному [PlayerView] */
    fun bindTo(view: PlayerView, url: String)

    /** Отвязывает плеер от view, не освобождая ресурсы */
    fun unbind()

    /** Пауза без освобождения ресурсов */
    fun pause()

    /** Возобновление воспроизведения */
    fun resume()

    /** Полное освобождение */
    fun release()
}