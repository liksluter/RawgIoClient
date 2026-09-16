package mr.liks.core.network.impl.client

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.HttpCallValidatorConfig
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import mr.liks.core.network.impl.exception.NetworkException
import timber.log.Timber

/** Перехватывает сетевые ошибки и преобразует их в [NetworkException] */
fun HttpCallValidatorConfig.convertErrorsToNetworkException() {
    handleResponseExceptionWithRequest { cause, request ->
        val url = request.url.toString()

        throw when (cause) {
            is ResponseException -> {
                val status = cause.response.status
                val body = runCatching { cause.response.bodyAsText() }.getOrNull()
                Timber.w(cause, "HTTP %d for %s", status.value, url)

                when (status.value) {
                    in 400..499 -> NetworkException.ClientError(status.value, body, cause)
                    in 500..599 -> NetworkException.ServerError(status.value, body, cause)
                    else -> NetworkException.Unknown(cause)
                }
            }

            is HttpRequestTimeoutException,
            is ConnectTimeoutException,
            is SocketTimeoutException -> {
                Timber.w(cause, "Request timed out: %s", url)
                NetworkException.Timeout(cause)
            }

            is SerializationException -> {
                Timber.w(cause, "Failed to parse response: %s", url)
                NetworkException.Serialization(cause)
            }

            is IOException -> {
                Timber.w(cause, "No connection: %s", url)
                NetworkException.NoConnection(cause)
            }

            is NetworkException -> cause

            else -> {
                Timber.e(cause, "Unknown network error: %s", url)
                NetworkException.Unknown(cause)
            }
        }
    }
}