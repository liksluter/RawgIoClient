package mr.liks.rawgioclient.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.rememberNavBackStack
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import kotlinx.coroutines.launch
import mr.liks.core.navigation.AppNavigator
import mr.liks.core.navigation.AppRoute
import mr.liks.core.navigation.TopLevelRoute
import mr.liks.rawgioclient.R
import mr.liks.rawgioclient.presentation.component.BottomBar
import mr.liks.rawgioclient.presentation.component.CollapsibleTopBar
import mr.liks.rawgioclient.presentation.component.TopBarCollapsedHeight
import mr.liks.rawgioclient.presentation.component.TopBarExpandedHeight
import mr.liks.rawgioclient.presentation.component.shouldShowBottomBar
import mr.liks.rawgioclient.presentation.component.shouldShowTopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(TopLevelRoute.Feed)
    val navigator = remember(backStack) { AppNavigator(backStack) }
    val currentRoute = backStack.lastOrNull()
    val hazeState = remember { HazeState() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val thresholdPx = remember(density) {
        with(density) { (TopBarExpandedHeight - TopBarCollapsedHeight).toPx() }
    }

    val collapseFraction by remember(listState, thresholdPx) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / thresholdPx).coerceIn(0f, 1f)
        }
    }

    val showScrollToTop by remember(listState) {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 ||
                    listState.firstVisibleItemScrollOffset > 0
        }
    }

    val showTopBar = (currentRoute as AppRoute).shouldShowTopBar()
    val showBottomBar = currentRoute.shouldShowBottomBar()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                BottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { navigator.switchTab(it) },
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                SmallFloatingActionButton(
                    onClick = { scope.launch { listState.animateScrollToItem(0) } },
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "К началу")
                }
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            MainNavHost(
                navigator = navigator,
                hazeState = hazeState,
                listState = listState,
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + TopBarExpandedHeight,
                    bottom = innerPadding.calculateBottomPadding(),
                ),
                modifier = Modifier.fillMaxSize(),
            )

            if (showTopBar) {
                CollapsibleTopBar(
                    title = stringResource(R.string.app_name),
                    collapseFraction = collapseFraction,
                    hazeState = hazeState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}