package mr.liks.core.common.applogger

import mr.liks.core.common.applocagger.AppLogger
import mr.liks.core.common.applocagger.NoOpAppLogger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

/** Тесты на [NoOpAppLogger] */
class NoOpAppLoggerTest {
    private val logger: AppLogger = NoOpAppLogger()

    @Test
    fun `all methods are no-op and never throw`() {
        assertDoesNotThrow {
            logger.d("debug %s", "arg")
            logger.i("info %s", "arg")
            logger.w(null, "warn %s", "arg")
            logger.w(RuntimeException("boom"), "warn with throwable")
            logger.e(null, "error %s", "arg")
            logger.e(RuntimeException("boom"), "error with throwable")
        }
    }
}