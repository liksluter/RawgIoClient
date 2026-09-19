package mr.liks.core.common.ext

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import mr.liks.core.common.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** Тесты на FlowExt.kt */
class FlowExtTest {
    @Test
    fun `asResult emits Loading then Success values`() = runTest {
        val results = flowOf(1, 2, 3).asResult().toList()

        assertEquals(4, results.size)
        assertEquals(Result.Loading, results[0])
        assertEquals(Result.Success(1), results[1])
        assertEquals(Result.Success(2), results[2])
        assertEquals(Result.Success(3), results[3])
    }

    @Test
    fun `asResult emits Loading then Error on exception`() = runTest {
        val exception = RuntimeException("boom")
        val source = flow<Int> { throw exception }

        val results = source.asResult().toList()

        assertEquals(2, results.size)
        assertEquals(Result.Loading, results[0])
        assertTrue(results[1] is Result.Error)
        assertSame(exception, (results[1] as Result.Error).exception)
    }

    @Test
    fun `asResult emits successes before the error`() = runTest {
        val exception = RuntimeException("boom")
        val source = flow {
            emit(1)
            emit(2)
            throw exception
        }

        val results = source.asResult().toList()

        assertEquals(4, results.size)
        assertEquals(Result.Loading, results[0])
        assertEquals(Result.Success(1), results[1])
        assertEquals(Result.Success(2), results[2])
        assertTrue(results[3] is Result.Error)
    }

    @Test
    fun `asResult on empty flow emits only Loading`() = runTest {
        val results = flowOf<Int>().asResult().toList()

        assertEquals(1, results.size)
        assertEquals(Result.Loading, results[0])
    }
}