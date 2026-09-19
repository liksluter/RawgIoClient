package mr.liks.core.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

/** Тесты на [DefaultDispatchersProvider] */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultDispatchersProviderTest {
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `main returns Dispatchers Main`() {
        val provider = DefaultDispatchersProvider()
        assertEquals(Dispatchers.Main, provider.main)
    }

    @Test
    fun `io returns Dispatchers IO`() {
        val provider = DefaultDispatchersProvider()
        assertEquals(Dispatchers.IO, provider.io)
    }

    @Test
    fun `default returns Dispatchers Default`() {
        val provider = DefaultDispatchersProvider()
        assertEquals(Dispatchers.Default, provider.default)
    }

    @Test
    fun `all dispatchers are non-null`() {
        val provider = DefaultDispatchersProvider()
        assertNotNull(provider.main)
        assertNotNull(provider.io)
        assertNotNull(provider.default)
    }
}