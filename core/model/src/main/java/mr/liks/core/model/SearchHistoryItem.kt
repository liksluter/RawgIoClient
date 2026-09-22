package mr.liks.core.model

/**
 * Элемент истории поиска
 *
 * @property id id записи в БД
 * @property query текст запроса
 * @property searchedAt время последнего поиска по этому запросу
 */
data class SearchHistoryItem(
    val id: Long,
    val query: String,
    val searchedAt: Long
)