package mr.liks.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.PlatformIcon

/**
 * Горизонтальный список иконок платформ
 *
 * @param platforms список платформ в формате [PlatformIcon]
 * @param modifier модификатор
 */
@Composable
fun PlatformIconRow(
    platforms: List<PlatformIcon>,
    modifier: Modifier = Modifier
) {
    if (platforms.isEmpty()) return

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
    ) {
        platforms.forEach { platform ->
            if (platform.iconUrl != null) {
                AsyncImage(
                    model = platform.iconUrl,
                    contentDescription = platform.name,
                    modifier = Modifier.size(ICON_SIZE),
                    contentScale = ContentScale.Fit
                )
            } else {
                PlatformFallback(name = platform.name, modifier = Modifier.size(ICON_SIZE))
            }
        }
    }
}

@Composable
private fun PlatformFallback(
    name: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraSmall
            )
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(2).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

private val ICON_SIZE = 24.dp

@Preview(showBackground = true)
@Composable
private fun PlatformIconRowPreview() {
    PlatformIconRow(
        platforms = listOf(
            PlatformIcon(id = 1, name = "PC", iconUrl = null),
            PlatformIcon(id = 2, name = "PlayStation 3", iconUrl = null),
            PlatformIcon(id = 3, name = "Xbox Series S/X", iconUrl = null),
            PlatformIcon(id = 4, name = "macOS", iconUrl = null),
            PlatformIcon(id = 5, name = "Nintendo Switch", iconUrl = null),
            PlatformIcon(id = 6, name = "Apple Macintosh", iconUrl = null),
        )
    )
}