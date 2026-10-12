package mr.liks.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Обёртка над SnackbarHostState
 *
 * @property hostState состояние [androidx.compose.material3.SnackbarHost]
 */
class SnackbarController(
    val hostState: SnackbarHostState,
) {
    suspend fun show(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
    ): SnackbarResult = hostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction,
        duration = duration,
    )
}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("SnackbarController не предоставлен")
}