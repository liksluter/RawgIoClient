package mr.liks.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.ui.R

/** Футер ленты, отображается при ошибке догрузки следующей страницы */
@Composable
fun ErrorFooter(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = RawgTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.small)
    ) {
        Text(
            text = message ?: stringResource(R.string.on_load_error_default_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = RawgTheme.spacing.large)
        )
        TextButton(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorFooterPreview() {
    ErrorFooter(onRetry = {}, message = "Не удалось загрузить данные")
}