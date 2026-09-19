package mr.liks.core.common.ext

import mr.liks.core.common.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Тесты на ThrowableExt.kt */
class ThrowableExtTest {
    @Test
    fun `toUserMessage returns exception message when non-blank`() {
        assertEquals("boom", RuntimeException("boom").toUserMessage())
    }

    @Test
    fun `toUserMessage returns default fallback when message is null`() {
        assertEquals("Что-то пошло не так", RuntimeException().toUserMessage())
    }

    @Test
    fun `toUserMessage returns default fallback when message is blank`() {
        assertEquals("Что-то пошло не так", RuntimeException("   ").toUserMessage())
    }

    @Test
    fun `toUserMessage returns custom fallback`() {
        assertEquals("custom", RuntimeException().toUserMessage("custom"))
    }

    @Test
    fun `orThrow returns data for Success`() {
        assertEquals(42, Result.Success(42).orThrow())
    }

    @Test
    fun `orThrow rethrows original exception for Error`() {
        val exception = RuntimeException("boom")
        val thrown = assertThrows<RuntimeException> { Result.Error(exception).orThrow() }
        assertSame(exception, thrown)
    }

    @Test
    fun `orThrow throws IllegalStateException for Loading`() {
        assertThrows<IllegalStateException> { Result.Loading.orThrow() }
    }
}