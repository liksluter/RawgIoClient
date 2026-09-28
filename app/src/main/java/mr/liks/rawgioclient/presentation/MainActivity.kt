package mr.liks.rawgioclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mr.liks.core.designsystem.theme.RawgTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: ThemeViewModel = koinViewModel()
            val themeState by appViewModel.themeState.collectAsStateWithLifecycle()

            RawgTheme(
                themeMode = themeState.themeMode,
                dynamicColor = themeState.dynamicColor
            ) {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}