package mr.liks.feature.settings.impl.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.feature.settings.impl.R

/** Секция "О приложении" */
@Composable
fun AboutSection(
    appVersion: String,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val rawgIoUrl = stringResource(R.string.rawg_io_url)

    Column(modifier = modifier.fillMaxWidth()) {
        AboutRow(
            title = stringResource(R.string.version),
            value = appVersion
        )
        AboutRow(
            title = stringResource(R.string.data_source_service),
            value = stringResource(R.string.rawg_io_domain),
            onClick = { onOpenUrl(rawgIoUrl) }
        )
    }
}

@Composable
private fun AboutRow(
    title: String,
    value: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .padding(
                horizontal = RawgTheme.spacing.large,
                vertical = RawgTheme.spacing.medium
            ),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}