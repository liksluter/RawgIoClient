package mr.liks.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import mr.liks.core.designsystem.token.LocalElevation
import mr.liks.core.designsystem.token.LocalSpacing
import mr.liks.core.designsystem.token.RawgElevation
import mr.liks.core.designsystem.token.RawgElevationDefault
import mr.liks.core.designsystem.token.RawgSpacing
import mr.liks.core.designsystem.token.RawgSpacingDefault

private val LightColors = lightColorScheme(
    primary = primary_light,
    onPrimary = onPrimary_light,
    primaryContainer = primaryContainer_light,
    onPrimaryContainer = onPrimaryContainer_light,
    secondary = secondary_light,
    onSecondary = onSecondary_light,
    secondaryContainer = secondaryContainer_light,
    onSecondaryContainer = onSecondaryContainer_light,
    tertiary = tertiary_light,
    onTertiary = onTertiary_light,
    error = error_light,
    onError = onError_light,
    background = background_light,
    onBackground = onBackground_light,
    surface = surface_light,
    onSurface = onSurface_light,
    surfaceVariant = surfaceVariant_light,
    onSurfaceVariant = onSurfaceVariant_light,
    outline = outline_light
)

private val DarkColors = darkColorScheme(
    primary = primary_dark,
    onPrimary = onPrimary_dark,
    primaryContainer = primaryContainer_dark,
    onPrimaryContainer = onPrimaryContainer_dark,
    secondary = secondary_dark,
    onSecondary = onSecondary_dark,
    secondaryContainer = secondaryContainer_dark,
    onSecondaryContainer = onSecondaryContainer_dark,
    tertiary = tertiary_dark,
    onTertiary = onTertiary_dark,
    error = error_dark,
    onError = onError_dark,
    background = background_dark,
    onBackground = onBackground_dark,
    surface = surface_dark,
    onSurface = onSurface_dark,
    surfaceVariant = surfaceVariant_dark,
    onSurfaceVariant = onSurfaceVariant_dark,
    outline = outline_dark
)

/**
 * Тема приложения
 *
 * @param themeMode режим из настроек: [ThemeMode.System], [ThemeMode.Light],
 *   [ThemeMode.Dark]. По умолчанию — [ThemeMode.System]
 * @param dynamicColor использовать ли Material You (Android 12+)
 * @param content содержимое.
 */
@Composable
fun RawgTheme(
    themeMode: ThemeMode = ThemeMode.System,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    val colorScheme = when {
        // В preview не подключаем динамические цвета — иначе рендер нестабилен.
        LocalInspectionMode.current -> if (darkTheme) DarkColors else LightColors

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    CompositionLocalProvider(
        LocalSpacing provides RawgSpacingDefault,
        LocalElevation provides RawgElevationDefault
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = RawgTypography,
            shapes = RawgShapes,
            content = content
        )
    }
}

/** Геттеры для RawgSpacing и RawgElevation */
object RawgTheme {
    val spacing: RawgSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current

    val elevation: RawgElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalElevation.current
}