package mr.liks.core.common

/** Результат операции - успех или ошибка */
sealed interface Result<out T> {
    /**
     * Успех
     *
     * @param T тип успешного значения
     * @property data данные результата
     */
    data class Success<out T>(val data: T) : Result<T>

    /**
     * Ошибка
     *
     * @property exception исключение
     * @property message сообщение
     */
    data class Error(
        val exception: Throwable,
        val message: String? = exception.message
    ) : Result<Nothing>

    /** Состояние загрузки, результата ещё нет (для стримов) */
    data object Loading : Result<Nothing>

    companion object {
        inline fun <T> runCatching(
            block: () -> T
        ): Result<T> = try {
            Success(block())
        } catch (e: Throwable) {
            Error(e)
        }

        suspend inline fun <T> runCatchingSuspend(
            crossinline block: suspend () -> T
        ): Result<T> = try {
            Success(block())
        } catch (e: Throwable) {
            Error(e)
        }

        inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
            is Success -> Success(transform(data))
            is Error -> this
            is Loading -> Loading
        }

        inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
            if (this is Success) action(data)
            return this
        }

        inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
            if (this is Error) action(exception)
            return this
        }

        fun <T> Result<T>.getOrNull(): T? = (this as? Success)?.data

        fun <T> Result<T>.getOrDefault(default: T): T = (this as? Success)?.data ?: default

        fun <T> Result<T>.getOrThrow(): T = when (this) {
            is Success -> data
            is Error -> throw exception
            is Loading -> error("Result is still loading")
        }
    }
}