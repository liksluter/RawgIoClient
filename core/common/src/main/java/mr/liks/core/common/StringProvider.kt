package mr.liks.core.common

import android.content.Context

/**
 * Провайдер строк из xml реусрсов
 *
 * @property context контекст
 */
class StringProvider(
    private val context: Context
) {
    /** @return строка по [resId] */
    fun getString(resId: Int) = context.getString(resId)
}