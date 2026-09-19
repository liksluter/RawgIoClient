package mr.liks.core.common.applocagger

/** No-op логгер для тестов и preview */
class NoOpAppLogger : AppLogger {
    override fun d(message: String, vararg args: Any) = Unit
    override fun i(message: String, vararg args: Any) = Unit
    override fun w(throwable: Throwable?, message: String, vararg args: Any) = Unit
    override fun e(throwable: Throwable?, message: String, vararg args: Any) = Unit
}