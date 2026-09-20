package mr.liks.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Обёртка над [NavBackStack] с операциями навигации уровня приложения
 *
 * @property backStack бэк стэк
 */
class AppNavigator(
    private val backStack: NavBackStack<NavKey>
) {
    /** Текущий маршрут */
    val currentRoute: AppRoute?
        get() = backStack.lastOrNull() as? AppRoute

    /** Может ли пользователь вернуться назад */
    val canGoBack: Boolean
        get() = backStack.size > 1

    /** Открыть экран поверх текущего */
    fun navigate(route: AppRoute) {
        backStack.add(route)
    }

    /** Заменить текущий экран, не увеличивая стек */
    fun replace(route: AppRoute) {
        if (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
        backStack.add(route)
    }

    /** Вернуться назад */
    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    /** Переключение между нижними табами */
    fun switchTab(route: AppRoute) {
        val existingIndex = backStack.indexOfFirst { it == route }
        if (existingIndex >= 0) {
            while (backStack.size > existingIndex + 1) {
                backStack.removeAt(backStack.lastIndex)
            }
            return
        }

        if (backStack.isNotEmpty()) {
            backStack.removeAt(0)
        }
        backStack.add(0, route)
    }

    /** Полный сброс до корневого таба */
    fun resetTo(route: AppRoute) {
        while (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
        backStack.add(route)
    }

    /** Очищает всё, что выше текущего корня */
    fun popToRoot() {
        while (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}