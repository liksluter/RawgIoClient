package mr.liks.feature.settings.impl.presentation

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mr.liks.core.designsystem.theme.RawgTheme
import mr.liks.core.model.ThemeMode
import mr.liks.feature.settings.impl.presentation.component.AboutSection
import mr.liks.feature.settings.impl.presentation.component.CacheSizeRow
import mr.liks.feature.settings.impl.presentation.component.SettingsToggleRow
import org.koin.androidx.compose.koinViewModel
import androidx.core.net.toUri
import mr.liks.feature.settings.impl.R

/** Экран настроек */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SettingsEffect.OpenUrl -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                    runCatching { context.startActivity(intent) }
                }
                is SettingsEffect.ShowSnackbar -> Unit // TODO: snackbar host
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = RawgTheme.spacing.small)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(RawgTheme.spacing.extraSmall)
        ) {
            item("header_appearance") {
                SectionHeader(stringResource(R.string.appearance))
            }

            item("theme") {
                ThemeSelector(
                    current = uiState.themeMode,
                    onSelect = { viewModel.onIntent(SettingsIntent.SetTheme(it)) }
                )
            }

            item("dynamic_color") {
                SettingsToggleRow(
                    title = stringResource(R.string.dynamic_colors_title),
                    subtitle = if (uiState.supportsDynamicColor) {
                        stringResource(R.string.dynamic_colors_subtitle_on_support)
                    } else {
                        stringResource(R.string.dynamic_colors_subtitle_on_disallow)
                    },
                    checked = uiState.dynamicColor,
                    onCheckedChange = {
                        viewModel.onIntent(SettingsIntent.SetDynamicColor(it))
                    },
                    enabled = uiState.supportsDynamicColor
                )
            }

            item("divider_1") { HorizontalDivider() }

            item("header_storage") {
                SectionHeader(stringResource(R.string.storage_title))
            }

            item("cache") {
                CacheSizeRow(
                    sizeBytes = uiState.cacheSizeBytes,
                    isClearing = uiState.isClearingCache,
                    onClear = { viewModel.onIntent(SettingsIntent.ClearCache) }
                )
            }

            item("divider_2") { HorizontalDivider() }

            item("header_about") {
                SectionHeader(stringResource(R.string.about_title))
            }

            item("about") {
                AboutSection(
                    appVersion = uiState.appVersion,
                    onOpenUrl = { viewModel.onIntent(SettingsIntent.OpenUrl(it)) }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(
            horizontal = RawgTheme.spacing.large,
            vertical = RawgTheme.spacing.medium
        )
    )
}

@Composable
private fun ThemeSelector(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ThemeMode.entries.forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = RawgTheme.spacing.large,
                        vertical = RawgTheme.spacing.small
                    ),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                androidx.compose.material3.RadioButton(
                    selected = current == mode,
                    onClick = { onSelect(mode) }
                )
                Text(
                    text = when (mode) {
                        ThemeMode.System -> stringResource(R.string.system_theme)
                        ThemeMode.Light -> stringResource(R.string.light_theme)
                        ThemeMode.Dark -> stringResource(R.string.dark_theme)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = RawgTheme.spacing.small)
                )
            }
        }
    }
}