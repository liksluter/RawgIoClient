package mr.liks.rawgioclient

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import mr.liks.core.navigation.AppNavigator
import mr.liks.core.navigation.AppRoute

@Composable
fun AppNavHost(
    navigator: AppNavigator,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = navigator.backStack(),
        modifier = modifier,
        entryProvider = { route ->
            when (route) {
                AppRoute.Feed -> NavEntry(route) {

                }
                AppRoute.Search -> NavEntry(route) {

                }
                AppRoute.Settings -> NavEntry(route) {

                }
                else -> error("Unknown route: $route")
            }
        }
    )
}