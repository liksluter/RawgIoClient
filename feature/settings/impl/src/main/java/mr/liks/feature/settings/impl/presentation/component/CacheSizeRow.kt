package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.feature.settings.impl.R
import java.util.Locale

/** Строка настроек "Размер кеша" с кнопкой очистки */
@Composable
fun CacheSizeRow(
    sizeBytes: Long,
    isClearing: Boolean,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = RawgTheme.spacing.large,
                vertical = RawgTheme.spacing.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.cache_size_title),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = formatBytes(sizeBytes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isClearing) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            TextButton(
                onClick = onClear,
                enabled = sizeBytes > 0
            ) {
                Text(stringResource(R.string.clear))
            }
        }
    }
}

internal fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 Б"
    val kb = 1024.0
    val mb = kb * 1024
    val gb = mb * 1024

    return when {
        bytes >= gb -> String.format(Locale.US, "%.1f ГБ", bytes / gb)
        bytes >= mb -> String.format(Locale.US, "%.1f МБ", bytes / mb)
        bytes >= kb -> String.format(Locale.US, "%.1f КБ", bytes / kb)
        else -> "$bytes Б"
    }
}