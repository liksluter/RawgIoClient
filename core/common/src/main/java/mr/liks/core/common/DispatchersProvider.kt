package mr.liks.core.common

import kotlinx.coroutines.CoroutineDispatcher

/** Абстракция над [kotlinx.coroutines.Dispatchers] для тестируемости */
interface DispatchersProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}