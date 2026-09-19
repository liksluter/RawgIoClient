package mr.liks.core.common.ext

import java.util.Locale

/** Форматирует рейтинг (0.0 … 5.0) в строку вида "4.5" */
fun Double.toRatingString(): String =
    String.format(Locale.US, "%.1f", this)

/** Обрезает строку до [max] символов, добавляя многоточие, если она длиннее */
fun String.truncate(max: Int): String =
    if (length <= max) this else take(max).trimEnd() + "…"

/**
 * Нормализует поисковый запрос: обрезает пробелы по краям, схлопывает
 * множественные пробелы внутри, приводит к нижнему регистру для сравнения.
 */
fun String.normalizeQuery(): String =
    trim().replace(Regex("\\s+"), " ").lowercase(Locale.ROOT)

/** @return `true`, если строка пустая или состоит только из пробелов. */
fun String?.isNullOrBlankOrEmpty(): Boolean = this.isNullOrBlank()