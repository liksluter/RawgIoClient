package mr.liks.core.common

import kotlinx.coroutines.test.runTest
import mr.liks.core.common.Result.Companion.getOrDefault
import mr.liks.core.common.Result.Companion.getOrNull
import mr.liks.core.common.Result.Companion.getOrThrow
import mr.liks.core.common.Result.Companion.map
import mr.liks.core.common.Result.Companion.onError
import mr.liks.core.common.Result.Companion.onSuccess
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows

/** Тесты на [Result] */
class ResultTest {
    @Test
    fun `runCatching returns Success when block returns value`() {
        val result = Result.runCatching { 42 }
        assertEquals(Result.Success(42), result)
    }

    @Test
    fun `runCatching returns Error when block throws`() {
        val exception = RuntimeException("boom")
        val result: Result<Int> = Result.runCatching { throw exception }
        assertTrue(result is Result.Error)
        assertSame(exception, (result as Result.Error).exception)
    }

    @Test
    fun `runCatching propagates result type`() {
        val result: Result<String> = Result.runCatching { "hello" }
        assertEquals(Result.Success("hello"), result)
    }

    @Test
    fun `runCatchingSuspend returns Success`() = runTest {
        val result = Result.runCatchingSuspend { 42 }
        assertEquals(Result.Success(42), result)
    }

    @Test
    fun `runCatchingSuspend returns Error`() = runTest {
        val exception = IllegalStateException("nope")
        val result: Result<Int> = Result.runCatchingSuspend { throw exception }
        assertTrue(result is Result.Error)
        assertSame(exception, (result as Result.Error).exception)
    }

    @Test
    fun `map transforms Success`() {
        val result = Result.Success(1).map { it + 1 }
        assertEquals(Result.Success(2), result)
    }

    @Test
    fun `map passes through Error unchanged`() {
        val error: Result<Int> = Result.Error(RuntimeException("x"))
        val result = error.map { it * 2 }
        assertSame(error, result)
    }

    @Test
    fun `map passes through Loading unchanged`() {
        val result = Result.Loading.map { 42 }
        assertEquals(Result.Loading, result)
    }

    @Test
    fun `onSuccess is invoked for Success`() {
        var captured: Int? = null
        Result.Success(7).onSuccess { captured = it }
        assertEquals(7, captured)
    }

    @Test
    fun `onSuccess is not invoked for Error`() {
        var called = false
        Result.Error(RuntimeException("x")).onSuccess { called = true }
        assertFalse(called)
    }

    @Test
    fun `onSuccess is not invoked for Loading`() {
        var called = false
        Result.Loading.onSuccess { called = true }
        assertFalse(called)
    }

    @Test
    fun `onSuccess returns same instance for chaining`() {
        val result = Result.Success(1)
        assertSame(result, result.onSuccess { })
    }

    @Test
    fun `onError is invoked for Error`() {
        var captured: Throwable? = null
        val exception = RuntimeException("x")
        Result.Error(exception).onError { captured = it }
        assertSame(exception, captured)
    }

    @Test
    fun `onError is not invoked for Success`() {
        var called = false
        Result.Success(1).onError { called = true }
        assertFalse(called)
    }

    @Test
    fun `getOrNull returns value for Success`() {
        assertEquals(1, Result.Success(1).getOrNull())
    }

    @Test
    fun `getOrNull returns null for Error`() {
        assertNull(Result.Error(RuntimeException()).getOrNull())
    }

    @Test
    fun `getOrNull returns null for Loading`() {
        assertNull(Result.Loading.getOrNull())
    }

    @Test
    fun `getOrDefault returns value for Success`() {
        assertEquals(1, Result.Success(1).getOrDefault(0))
    }

    @Test
    fun `getOrDefault returns default for Error`() {
        assertEquals(0, Result.Error(RuntimeException()).getOrDefault(0))
    }

    @Test
    fun `getOrDefault returns default for Loading`() {
        assertEquals(0, Result.Loading.getOrDefault(0))
    }

    @Test
    fun `getOrThrow returns value for Success`() {
        assertEquals(1, Result.Success(1).getOrThrow())
    }

    @Test
    fun `getOrThrow throws original exception for Error`() {
        val exception = RuntimeException("x")
        val thrown = assertThrows<RuntimeException> { Result.Error(exception).getOrThrow() }
        assertSame(exception, thrown)
    }

    @Test
    fun `getOrThrow throws IllegalStateException for Loading`() {
        assertThrows<IllegalStateException> { Result.Loading.getOrThrow() }
    }

    @Test
    fun `Error uses exception message by default`() {
        val error = Result.Error(RuntimeException("boom"))
        assertEquals("boom", error.message)
    }

    @Test
    fun `Error message is null when exception has no message`() {
        val error = Result.Error(RuntimeException())
        assertNull(error.message)
    }
}