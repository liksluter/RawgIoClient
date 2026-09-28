package mr.liks.core.model

/**
 * Настройки приложения
 *
 * @property themeMode тема: системная, светлая, тёмная
 * @property dynamicColor использовать ли динамические цвета Material You (Android 12+)
 * @property cacheSizeBytes текущий размер дискового кеша в байтах
 */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val dynamicColor: Boolean = false,
    val cacheSizeBytes: Long = 0L
)