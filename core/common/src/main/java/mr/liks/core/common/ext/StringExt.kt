package mr.liks.core.common.ext

import java.util.Locale

/** Форматирует рейтинг в строку вида */
fun Double.toRatingString(): String = String.format(Locale.US, "%.1f", this)

/**
 * Нормализует поисковый запрос: обрезает пробелы по краям, схлопывает пробелы внутри,
 * приводит к нижнему регистру для сравнения
 */
fun String.normalizeQuery(): String =
    trim().replace(Regex("\\s+"), " ").lowercase(Locale.ROOT)