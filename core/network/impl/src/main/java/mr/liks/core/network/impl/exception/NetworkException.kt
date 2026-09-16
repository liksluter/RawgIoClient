package mr.liks.core.network.impl.exception

/** Ошибки сетевого слоя */
sealed class NetworkException(
    message: String,
    cause: Throwable? = null
): Exception(message, cause) {
    /** Нет соединения */
    class NoConnection(cause: Throwable? = null) :
        NetworkException("No network connection", cause)

    /** Таймаут запроса */
    class Timeout(cause: Throwable? = null) :
        NetworkException("Request timed out", cause)

    /** HTTP 4xx */
    class ClientError(
        val code: Int,
        val body: String?,
        cause: Throwable? = null
    ) : NetworkException("Client error $code", cause)

    /** HTTP 5xx */
    class ServerError(
        val code: Int,
        val body: String?,
        cause: Throwable? = null
    ) : NetworkException("Server error $code", cause)

    /** Не удалось распарсить ответ */
    class Serialization(cause: Throwable? = null) :
        NetworkException("Failed to parse response", cause)

    /** Всё остальное */
    class Unknown(cause: Throwable? = null) :
        NetworkException("Unknown network error", cause)
}