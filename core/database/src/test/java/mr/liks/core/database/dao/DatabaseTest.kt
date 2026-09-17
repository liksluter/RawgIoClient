package mr.liks.core.database.dao

import android.content.Context
import androidx.room.Room
import mr.liks.core.database.RawgDatabase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.robolectric.RuntimeEnvironment
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

/** Базовый класс для тестов DAO */
@ExtendWith(RobolectricExtension::class)
abstract class DatabaseTest {
    protected lateinit var db: RawgDatabase
    protected lateinit var context: Context

    @BeforeEach
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        db = Room.inMemoryDatabaseBuilder(context, RawgDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @AfterEach
    fun teardown() {
        db.close()
    }
}