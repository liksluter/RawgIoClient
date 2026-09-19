package mr.liks.core.common.applogger

import mr.liks.core.common.applocagger.TimberAppLogger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import timber.log.Timber

/** Тесты на [TimberAppLogger] */
class TimberAppLoggerTest {
    private lateinit var tree: RecordingTree
    private lateinit var logger: TimberAppLogger

    @BeforeEach
    fun setUp() {
        tree = RecordingTree()
        Timber.plant(tree)
        logger = TimberAppLogger()
    }

    @AfterEach
    fun tearDown() {
        Timber.uproot(tree)
    }

    @Test
    fun `d delegates to Timber with formatted message`() {
        logger.d("hello %s", "world")

        val record = tree.records.single()
        assertEquals(LogLevel.DEBUG, record.level)
        assertEquals("hello world", record.message)
        assertNull(record.throwable)
    }

    @Test
    fun `i delegates to Timber with formatted message`() {
        logger.i("info %d", 42)

        val record = tree.records.single()
        assertEquals(LogLevel.INFO, record.level)
        assertEquals("info 42", record.message)
        assertNull(record.throwable)
    }

    @Test
    fun `w without throwable uses message overload`() {
        logger.w(null, "warn %s", "x")

        val record = tree.records.single()
        assertEquals(LogLevel.WARN, record.level)
        assertEquals("warn x", record.message)
        assertNull(record.throwable)
    }

    @Test
    fun `e without throwable uses message overload`() {
        logger.e(null, "err")

        val record = tree.records.single()
        assertEquals(LogLevel.ERROR, record.level)
        assertEquals("err", record.message)
        assertNull(record.throwable)
    }

    @Test
    fun `w with throwable passes it to Timber`() {
        val exception = RuntimeException("boom")
        logger.w(exception, "warn")

        val record = tree.records.single()
        assertEquals(LogLevel.WARN, record.level)
        assertSame(exception, record.throwable)
        assertEquals("warn", record.message.substringBefore('\n'))
        assertTrue(record.message.contains("java.lang.RuntimeException: boom"))
    }

    @Test
    fun `e with throwable passes it to Timber and formats args`() {
        val exception = RuntimeException("boom")
        logger.e(exception, "err %s %d", "!", 7)

        val record = tree.records.single()
        assertEquals(LogLevel.ERROR, record.level)
        assertSame(exception, record.throwable)

        assertEquals("err ! 7", record.message.substringBefore('\n'))
        assertTrue(record.message.contains("java.lang.RuntimeException: boom"))
    }

    private enum class LogLevel { DEBUG, INFO, WARN, ERROR }

    private data class Record(
        val level: LogLevel,
        val message: String,
        val throwable: Throwable?
    )

    private class RecordingTree : Timber.Tree() {
        val records = mutableListOf<Record>()

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val level = when (priority) {
                3 -> LogLevel.DEBUG
                4 -> LogLevel.INFO
                5 -> LogLevel.WARN
                6 -> LogLevel.ERROR
                else -> error("Unexpected priority: $priority")
            }
            records += Record(level, message, t)
        }
    }
}