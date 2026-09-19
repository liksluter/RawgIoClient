package mr.liks.core.common.ext

import mr.liks.core.common.Result

/** Извлекает сообщение из ошибки, если сообщение пустое — возвращает fallback */
fun Throwable.toUserMessage(fallback: String = "Что-то пошло не так"): String =
    message?.takeIf { it.isNotBlank() } ?: fallback

/** Аналог `getOrThrow()` */
fun <T> Result<T>.orThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error -> throw exception
    is Result.Loading -> error("Result is still loading")
}