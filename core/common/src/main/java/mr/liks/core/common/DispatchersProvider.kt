package mr.liks.core.common

import kotlinx.coroutines.CoroutineDispatcher

/** Абстракция над [kotlinx.coroutines.Dispatchers] для тестируемости */
interface DispatchersProvider {
    /** Диспетчер главного потока */
    val main: CoroutineDispatcher
    /** Диспетчер потоков ввода-вывода */
    val io: CoroutineDispatcher
    /** Диспетчер потоков для вычислений */
    val default: CoroutineDispatcher
}