package mr.liks.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

/** Тесты на [AppNavigator] */
class AppNavigatorTest {
    private lateinit var backStack: NavBackStack<NavKey>
    private lateinit var navigator: AppNavigator

    @BeforeEach
    fun setUp() {
        backStack = NavBackStack(TopLevelRoute.Feed)
        navigator = AppNavigator(backStack)
    }

    @Test
    fun `currentRoute returns first route when stack has one element`() {
        assertEquals(TopLevelRoute.Feed, navigator.currentRoute)
    }

    @Test
    fun `currentRoute returns last route when stack has multiple elements`() {
        backStack.add(TopLevelRoute.Search)
        assertEquals(TopLevelRoute.Search, navigator.currentRoute)
    }

    @Test
    fun `currentRoute returns null when stack is empty`() {
        backStack.clear()
        assertNull(navigator.currentRoute)
    }

    @Test
    fun `currentRoute returns null when top of stack is not an TopLevelRoute`() {
        backStack.add(object : NavKey {})
        assertNull(navigator.currentRoute)
    }

    @Test
    fun `canGoBack returns false when stack size is 1`() {
        assertFalse(navigator.canGoBack)
    }

    @Test
    fun `canGoBack returns true when stack size greater than 1`() {
        backStack.add(TopLevelRoute.Search)
        assertTrue(navigator.canGoBack)
    }

    @Test
    fun `navigate adds route to stack`() {
        navigator.navigate(TopLevelRoute.Search)
        assertEquals(2, backStack.size)
        assertEquals(TopLevelRoute.Search, backStack.last())
    }

    @Test
    fun `replace removes current route and adds new one`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.replace(TopLevelRoute.Settings)
        assertEquals(2, backStack.size)
        assertEquals(TopLevelRoute.Settings, backStack.last())
        assertEquals(TopLevelRoute.Feed, backStack.first())
    }

    @Test
    fun `replace on empty stack just adds route`() {
        backStack.clear()
        navigator.replace(TopLevelRoute.Settings)
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Settings, backStack.last())
    }

    @Test
    fun `goBack removes last route when size greater than 1`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.goBack()
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack.last())
    }

    @Test
    fun `goBack does nothing when size is 1`() {
        navigator.goBack()
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack.last())
    }

    @Test
    fun `goBack does nothing when stack is empty`() {
        backStack.clear()
        navigator.goBack()
        assertTrue(backStack.isEmpty())
    }

    @Test
    fun `switchTab to existing route removes routes above it`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.navigate(TopLevelRoute.Settings)
        navigator.switchTab(TopLevelRoute.Search)
        assertEquals(2, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack[0])
        assertEquals(TopLevelRoute.Search, backStack[1])
    }

    @Test
    fun `switchTab to new route replaces root and keeps rest`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.switchTab(TopLevelRoute.Settings)
        assertEquals(2, backStack.size)
        assertEquals(TopLevelRoute.Settings, backStack[0])
        assertEquals(TopLevelRoute.Search, backStack[1])
    }

    @Test
    fun `switchTab on empty stack adds route at index 0`() {
        backStack.clear()
        navigator.switchTab(TopLevelRoute.Settings)
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Settings, backStack[0])
    }

    @Test
    fun `resetTo clears stack and adds route`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.navigate(TopLevelRoute.Settings)
        navigator.resetTo(TopLevelRoute.Feed)
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack[0])
    }

    @Test
    fun `resetTo on empty stack adds route`() {
        backStack.clear()
        navigator.resetTo(TopLevelRoute.Feed)
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack[0])
    }

    @Test
    fun `popToRoot keeps only first route`() {
        navigator.navigate(TopLevelRoute.Search)
        navigator.navigate(TopLevelRoute.Settings)
        navigator.popToRoot()
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack[0])
    }

    @Test
    fun `popToRoot does nothing when already at root`() {
        navigator.popToRoot()
        assertEquals(1, backStack.size)
        assertEquals(TopLevelRoute.Feed, backStack[0])
    }

    @Test
    fun `popToRoot on empty stack does nothing`() {
        backStack.clear()
        navigator.popToRoot()
        assertTrue(backStack.isEmpty())
    }
}