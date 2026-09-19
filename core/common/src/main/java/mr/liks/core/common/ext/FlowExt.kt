package mr.liks.core.common.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import mr.liks.core.common.Result

/**
 * Преобразует [Flow]<T> в [Flow]<Result<T>>, оборачивая ошибки в [Result.Error],
 * эмитит [Result.Loading] перед первым значением
 *
 * @param T тип результата
 * @return флоу [Flow]<Result<T>>
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this
    .map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Error(it)) }