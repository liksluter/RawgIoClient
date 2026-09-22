package mr.liks.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mr.liks.core.designsystem.icon.RawgIcons
import mr.liks.core.designsystem.theme.RawgTheme

/**
 * Полноэкранная ошибка загрузки ленты
 *
 * @param message текст ошибки
 * @param onRetry колбэк повтора
 */
@Composable
fun FeedError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(RawgTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = RawgIcons.Error,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = RawgTheme.spacing.large,
                bottom = RawgTheme.spacing.large
            )
        )

        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedErrorPreview() {
    FeedError(
        message = "Не удалось загрузить ленту. Проверьте подключение.",
        onRetry = {}
    )
}