package mr.liks.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
 * Пустое состояние ленты
 *
 * @param modifier модификатор
 * @param message сообщение
 */
@Composable
fun FeedEmpty(
    modifier: Modifier = Modifier,
    message: String = "Здесь пока ничего нет"
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(RawgTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = RawgIcons.Image,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = RawgTheme.spacing.large)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedEmptyPreview() {
    FeedEmpty()
}