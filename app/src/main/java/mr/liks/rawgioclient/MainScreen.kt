package mr.liks.rawgioclient

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.rememberNavBackStack
import mr.liks.core.navigation.AppNavigator
import mr.liks.core.navigation.AppRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppRoute.Feed)
    val navigator = remember(backStack) { AppNavigator(backStack) }
    val currentRoute = backStack.lastOrNull()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if ((currentRoute as AppRoute).shouldShowTopBar()) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            if ((currentRoute as AppRoute).shouldShowBottomBar()) {
                BottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { navigator.switchTab(it) }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navigator = navigator,
            modifier = Modifier.padding(innerPadding)
        )
    }
}