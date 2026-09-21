package mr.liks.core.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Токены отступов */
@Immutable
data class RawgSpacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
    val huge: Dp = 32.dp,

    /** Горизонтальный отступ карточки ленты */
    val feedCardHorizontal: Dp = 12.dp,

    /** Вертикальный отступ между карточками ленты */
    val feedCardVertical: Dp = 16.dp,

    /** Внутренний отступ карточки */
    val cardInner: Dp = 12.dp
)

internal val RawgSpacingDefault = RawgSpacing()

val LocalSpacing = staticCompositionLocalOf { RawgSpacingDefault }