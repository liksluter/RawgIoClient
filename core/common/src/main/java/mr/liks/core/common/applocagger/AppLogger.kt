package mr.liks.core.common.applocagger

/** Враппер над Timber */
interface AppLogger {
    fun d(message: String, vararg args: Any)
    fun i(message: String, vararg args: Any)
    fun w(throwable: Throwable? = null, message: String, vararg args: Any)
    fun e(throwable: Throwable? = null, message: String, vararg args: Any)
}