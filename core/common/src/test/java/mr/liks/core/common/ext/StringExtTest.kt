package mr.liks.core.common.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** Тесты на StringExt.kt */
class StringExtTest {
    @Test
    fun `toRatingString formats with one decimal using US locale`() {
        assertEquals("4.5", 4.5.toRatingString())
        assertEquals("0.0", 0.0.toRatingString())
        assertEquals("5.0", 5.0.toRatingString())
        assertEquals("4.0", 4.0.toRatingString())
    }

    @Test
    fun `toRatingString rounds half up to one decimal`() {
        assertEquals("3.7", 3.66.toRatingString())
        assertEquals("3.6", 3.64.toRatingString())
    }

    @Test
    fun `toRatingString always uses dot as separator`() {
        // независимо от default locale
        val s = 1.5.toRatingString()
        assertTrue(s.contains('.'))
        assertFalse(s.contains(','))
    }

    @Test
    fun `normalizeQuery trims and lowercases`() {
        assertEquals("hello world", "  HELLO   World  ".normalizeQuery())
    }

    @Test
    fun `normalizeQuery collapses all whitespace runs`() {
        assertEquals("a b c", "a  b\tc\n".normalizeQuery())
    }

    @Test
    fun `normalizeQuery on empty string returns empty`() {
        assertEquals("", "   ".normalizeQuery())
    }
}