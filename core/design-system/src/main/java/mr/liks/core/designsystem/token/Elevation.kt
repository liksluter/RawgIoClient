package mr.liks.core.designsystem.token

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Токены теней */
@Immutable
data class RawgElevation(
    val none: Dp = 0.dp,
    val card: Dp = 4.dp,
    val dialog: Dp = 8.dp,
    val sheet: Dp = 12.dp,
    val topBar: Dp = 2.dp
)

internal val RawgElevationDefault = RawgElevation()

val LocalElevation = staticCompositionLocalOf { RawgElevationDefault }