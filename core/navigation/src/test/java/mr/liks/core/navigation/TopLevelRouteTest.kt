package mr.liks.core.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Тесты на [TopLevelRoute] */
class TopLevelRouteTest {
    @Test
    fun `ordered returns routes sorted by order`() {
        val ordered = TopLevelRoute.ordered
        assertEquals(3, ordered.size)
        assertEquals(TopLevelRoute.FEED, ordered[0])
        assertEquals(TopLevelRoute.SEARCH, ordered[1])
        assertEquals(TopLevelRoute.SETTINGS, ordered[2])
    }

    @Test
    fun `fromRoute returns correct enum for each route`() {
        assertEquals(TopLevelRoute.FEED, TopLevelRoute.fromRoute(AppRoute.Feed))
        assertEquals(TopLevelRoute.SEARCH, TopLevelRoute.fromRoute(AppRoute.Search))
        assertEquals(TopLevelRoute.SETTINGS, TopLevelRoute.fromRoute(AppRoute.Settings))
    }
}