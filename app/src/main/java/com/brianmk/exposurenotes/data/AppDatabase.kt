package com.brianmk.exposurenotes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "camera_maker_table")
data class CameraMaker(@PrimaryKey @ColumnInfo(name = "maker") val maker: String)

@Dao
interface CameraMakerDao {
    @Query ("SELECT * FROM camera_maker_table ORDER BY maker ASC")
    fun getAll() : LiveData<List<CameraMaker>>

    @Insert
    fun insertMaker(cameraMaker: CameraMaker)

    @Query ("DELETE FROM camera_maker_table")
    fun deleteMakers()
}

@Database(entities = [CameraMaker::class], version = 1)
abstract class CameraMakerDatabase : RoomDatabase() {
    abstract fun cameraMakerDao() : CameraMakerDao
}




/*
class CameraMakerRepository(private val cameraMakerDao: CameraMakerDao) {
    val allCameraMakers: LiveData<List<CameraMaker>> = cameraMakerDao.getAll()

    @WorkerThread
    suspend fun insert(cameraMaker: CameraMaker) {
        cameraMakerDao.insertMaker(cameraMaker)
    }
}
*/

