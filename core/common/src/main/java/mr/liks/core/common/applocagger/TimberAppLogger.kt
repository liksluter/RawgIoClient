package mr.liks.core.common.applocagger

import timber.log.Timber

/** Реализация [AppLogger] */
class TimberAppLogger : AppLogger {
    override fun d(message: String, vararg args: Any) = Timber.d(message, *args)
    override fun i(message: String, vararg args: Any) = Timber.i(message, *args)
    override fun w(throwable: Throwable?, message: String, vararg args: Any) {
        if (throwable != null) Timber.w(throwable, message, *args)
        else Timber.w(message, *args)
    }
    override fun e(throwable: Throwable?, message: String, vararg args: Any) {
        if (throwable != null) Timber.e(throwable, message, *args)
        else Timber.e(message, *args)
    }
}