package mr.liks.core.ui.effect

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

/** Состояние прогресса шиммера */
private val LocalShimmerProgress = compositionLocalOf<State<Float>> {
    mutableFloatStateOf(0f)
}

/** Обёртка над группой плейсхолдеров */
@Composable
fun FeedShimmer(
    durationMs: Int = 1200,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progressState: State<Float> = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    CompositionLocalProvider(LocalShimmerProgress provides progressState) {
        content()
    }
}

/** Заливает элемент базовым цветом и поверх рисует бегущий блик */
@Composable
fun Modifier.shimmer(
    shape: Shape = RectangleShape
): Modifier {
    val progressState = LocalShimmerProgress.current
    val baseColor = MaterialTheme.colorScheme.surfaceVariant
    val highlightColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
    val highlightTransparent = Color.White.copy(alpha = 0f)

    return this
        .clip(shape)
        .background(baseColor)
        .drawBehind {
            val progress = progressState.value

            val width = size.width
            if (width <= 0f) return@drawBehind

            val shimmerWidth = width * 0.6f
            val startX = -shimmerWidth + progress * (width + shimmerWidth)

            val brush = Brush.linearGradient(
                colors = listOf(
                    highlightTransparent,
                    highlightColor,
                    highlightTransparent
                ),
                start = Offset(startX, 0f),
                end = Offset(startX + shimmerWidth, 0f)
            )
            drawRect(brush)
        }
}