package mr.liks.core.navigation

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/** Тесты на [TopLevelRouteInfo] */
class TopLevelRouteInfoTest {
    @Test
    fun `ordered returns routes sorted by order`() {
        val ordered = TopLevelRouteInfo.ordered
        assertEquals(3, ordered.size)
        assertEquals(TopLevelRouteInfo.FEED, ordered[0])
        assertEquals(TopLevelRouteInfo.SEARCH, ordered[1])
        assertEquals(TopLevelRouteInfo.SETTINGS, ordered[2])
    }

    @Test
    fun `fromRoute returns correct enum for each route`() {
        assertEquals(TopLevelRouteInfo.FEED, TopLevelRouteInfo.fromRoute(TopLevelRoute.Feed))
        assertEquals(TopLevelRouteInfo.SEARCH, TopLevelRouteInfo.fromRoute(TopLevelRoute.Search))
        assertEquals(TopLevelRouteInfo.SETTINGS, TopLevelRouteInfo.fromRoute(TopLevelRoute.Settings))
    }
}