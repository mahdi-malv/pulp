package ir.malv.utils.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [PulpItem::class], exportSchema = false, version = 1)
abstract class PulpDatabase : RoomDatabase() {

    abstract fun pulpDao(): PulpDao

    companion object {

        private var instance: PulpDatabase? = null

        fun database(context: Context): PulpDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                PulpDatabase::class.java,
                "pulp.db"
            )
                .build()
                .also { instance = it }
        }
    }
}

@Entity(tableName = "log_item")
data class PulpItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "level")
    val level: String,

    @ColumnInfo(name = "tags")
    val tags: String,

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "data")
    val data: String,

    @ColumnInfo(name = "error")
    val error: String,

    @ColumnInfo(name = "time")
    val time: Long
)

@Dao
interface PulpDao {

    @Insert
    fun insert(p: PulpItem)

    @Query("select * from log_item")
    fun getAll(): LiveData<List<PulpItem>>

    @Query("delete from log_item;")
    fun clearAll()
}