package mr.liks.rawgioclient.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.chrisbanes.haze.HazeState
import mr.liks.core.navigation.AppNavigator
import mr.liks.core.navigation.TopLevelRoute
import mr.liks.feature.details.impl.presentation.GameDetailsScreen
import mr.liks.feature.detais.api.GameDetailsRoute
import mr.liks.feature.feed.api.FeedRoute
import mr.liks.feature.feed.impl.presentation.FeedScreen
import mr.liks.feature.settings.impl.presentation.SettingsScreen
import mr.liks.rawgioclient.presentation.component.TopBarExpandedHeight

@Composable
fun MainNavHost(
    navigator: AppNavigator,
    hazeState: HazeState,
    listState: LazyListState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = navigator.backStack(),
        modifier = modifier,
        entryProvider = { route ->
            when (route) {
                TopLevelRoute.Feed, FeedRoute -> NavEntry(route) {
                    FeedScreen(
                        onGameClick = { navigator.navigate(GameDetailsRoute(it)) },
                        hazeState = hazeState,
                        listState = listState,
                        contentPadding = PaddingValues(
                            top = contentPadding.calculateTopPadding() + TopBarExpandedHeight,
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is GameDetailsRoute -> NavEntry(route) {
                    GameDetailsScreen(
                        gameId = route.gameId,
                        onBack = navigator::goBack,
                        contentPadding = contentPadding
                    )
                }
                TopLevelRoute.Search -> NavEntry(route) { }
                TopLevelRoute.Settings -> NavEntry(route) {
                    SettingsScreen()
                }
                else -> error("Unknown route: $route")
            }
        }
    )
}