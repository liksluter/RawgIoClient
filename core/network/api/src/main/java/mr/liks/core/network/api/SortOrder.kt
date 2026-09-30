package mr.liks.core.network.api

/** Порядок сортировки с названием [ordering] */
enum class SortOrder(val ordering: String) {
    /** По названиею */
    NAME("name"),
    /** По дате релиза */
    RELEASED("released"),
    /** По дате добавления */
    ADDED("added"),
    /** По дате создания */
    CREATED("created"),
    /** По дате обновления */
    UPDATED("updated"),
    /** По рейтингу */
    RATING("rating"),
    /** По рейтингу в метакритике */
    METACRITIC("metacritic");

    /** @return строковое значние порядка сортировки в зависимости от направления [reverseSortOrder] */
    fun getOrdering(reverseSortOrder: Boolean) =
        ordering.takeIf { !reverseSortOrder } ?: "-$ordering"
}