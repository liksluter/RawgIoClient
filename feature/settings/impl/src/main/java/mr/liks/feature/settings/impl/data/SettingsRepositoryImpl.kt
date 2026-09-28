package mr.liks.feature.settings.impl.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import mr.liks.core.common.CacheManager
import mr.liks.core.common.DispatchersProvider
import mr.liks.core.model.AppSettings
import mr.liks.core.model.ThemeMode
import mr.liks.datastore.SettingsDataStore
import mr.liks.feature.settings.impl.domain.repository.SettingsRepository

/**
 * Реализация [SettingsRepository]
 *
 * @property dataStore храналище [SettingsDataStore]
 * @property cacheManager менеджер кеша
 * @property dispatchers диспетчеры
 * @param externalScope внешний скоуп для корутин
 */
class SettingsRepositoryImpl(
    private val dataStore: SettingsDataStore,
    private val cacheManager: CacheManager,
    private val dispatchers: DispatchersProvider,
    externalScope: CoroutineScope
) : SettingsRepository {
    override fun observeSettings(): Flow<AppSettings> {
        return combine(
            dataStore.flow,
            cacheSizeFlow()
        ) { settings, cacheSize ->
            settings.copy(cacheSizeBytes = cacheSize)
        }
            .flowOn(dispatchers.io)
    }

    override suspend fun setTheme(mode: ThemeMode) {
        withContext(dispatchers.io) {
            dataStore.setThemeMode(mode)
        }
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        withContext(dispatchers.io) {
            dataStore.setDynamicColor(enabled)
        }
    }

    override suspend fun getCacheSizeBytes(): Long =
        withContext(dispatchers.io) {
            cacheManager.getCacheSizeBytes()
        }

    override suspend fun clearCache() {
        withContext(dispatchers.io) {
            cacheManager.clearAll()
        }
    }

    private fun cacheSizeFlow(): Flow<Long> = flow {
        while (true) {
            emit(cacheManager.getCacheSizeBytes())
            delay(CACHE_REFRESH_INTERVAL_MS)
        }
    }

    private companion object {
        const val CACHE_REFRESH_INTERVAL_MS = 5_000L
    }
}