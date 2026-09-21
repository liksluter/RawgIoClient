package mr.liks.rawgioclient

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mr.liks.core.navigation.AppRoute
import mr.liks.core.navigation.TopLevelRoute

@Composable
fun BottomBar(
    currentRoute: AppRoute?,
    onTabSelected: (AppRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        TopLevelRoute.ordered.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) onTabSelected(tab.route)
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(tab.label) }
            )
        }
    }
}

fun AppRoute?.shouldShowBottomBar(): Boolean =
    TopLevelRoute.fromRoute(this) != null

fun AppRoute?.shouldShowTopBar(): Boolean =
    TopLevelRoute.fromRoute(this) != null