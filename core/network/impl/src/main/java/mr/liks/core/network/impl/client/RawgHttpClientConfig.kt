package mr.liks.core.network.impl.client

/**
 * Конфиг http клиента для сервиса rawg.io
 *
 * @property baseUrl базовый урл
 * @property apiKeyName имя параметра ключа api
 * @property apiKey ключ api
 * @property requestTimeout таймаут запроса
 * @property connectTimeout таймаут соединения
 * @property socketTimeout таймаут сокета
 * @property isDebug флаг отладки, `ture` если включена
 */
data class RawgHttpClientConfig(
    val baseUrl: String,
    val apiKeyName: String,
    val apiKey: String,
    val requestTimeout: Long = 30_000L,
    val connectTimeout: Long = 15_000L,
    val socketTimeout: Long = 30_000L,
    val isDebug: Boolean,
)