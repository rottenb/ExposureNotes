package com.brianmk.exposurenotes.data

import androidx.room.*

@Entity(tableName = "camera_maker_table")
data class CameraMaker(@PrimaryKey @ColumnInfo(name = "maker") val maker: String)

@Entity(tableName = "camera_model_table")
data class CameraModel(@PrimaryKey @ColumnInfo(name = "model") val model: String)

@Entity(tableName = "film_maker_table")
data class FilmMaker(@PrimaryKey @ColumnInfo(name = "maker") val maker: String)

@Entity(tableName = "film_model_table")
data class FilmModel(@PrimaryKey @ColumnInfo(name = "model") val model: String)

@Entity(tableName = "frame_info_table")
data class FrameInfo(@PrimaryKey @ColumnInfo(name = "number") val number: Int,
                     @ColumnInfo(name = "shutterIdx") val shutterIdx: Int,
                     @ColumnInfo(name = "apertureIdx") val apertureIdx: Int,
                     @ColumnInfo(name = "notes") val notes: String )

@Entity(tableName = "roll_info_table")
data class RollInfo(@PrimaryKey @ColumnInfo(name = "camera_manu") val cameraManu: String,
                    @ColumnInfo(name="camera_name") val cameraName: String,
                    @ColumnInfo(name="camera_format") val cameraFormat: String,
                    @ColumnInfo(name="camera_lensIdx") val cameraLensIdx: Int,
                    @ColumnInfo(name="camera_isFixed") val cameraIsFixed: Boolean,

                    @ColumnInfo(name="film_manu") val filmManu: String,
                    @ColumnInfo(name="film_name") val filmName: String,
                    @ColumnInfo(name="film_isoIdx") val filmIsoIdx: Int,
                    @ColumnInfo(name="film_frames") val filmFrames: Int,
                    @ColumnInfo(name="film_devIdx") val filmDevIdx: Int)
/*
data class RollInfo(@PrimaryKey @ColumnInfo(name = "camera_name") val cameraName: String,
                    @ColumnInfo(name="film_name") val filmName: String,
                    @ColumnInfo(name="isoIdx") val isoIdx: Int,
                    @ColumnInfo(name="devIdx") val devIdx: Int,
                    @ColumnInfo(name="frames") val frames: Int)
*/
// Entities


@Dao
interface CameraMakerDao {
    @Query ("SELECT * FROM camera_maker_table ORDER BY maker ASC")
    fun getAll() : List<CameraMaker>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cameraMaker: CameraMaker)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cameraMaker: CameraMaker)

    @Query ("DELETE FROM camera_maker_table")
    fun deleteAll()
}

@Dao
interface CameraModelDao {
    @Query ("SELECT * FROM camera_model_table ORDER BY model ASC")
    fun getAll() : List<CameraModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cameraModel: CameraModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(cameraModel: CameraModel)


    @Query ("DELETE FROM camera_model_table")
    fun deleteAll()
}

@Dao
interface FilmMakerDao {
    @Query ("SELECT * FROM film_maker_table ORDER BY maker ASC")
    fun getAll() : List<FilmMaker>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filmMaker: FilmMaker)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(filmMaker: FilmMaker)

    @Query ("DELETE FROM film_maker_table")
    fun deleteAll()
}

@Dao
interface FilmModelDao {
    @Query ("SELECT * FROM film_model_table ORDER BY model ASC")
    fun getAll() : List<FilmModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filmModel: FilmModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(filmModel: FilmModel)

    @Query ("DELETE FROM camera_model_table")
    fun deleteAll()
}

@Dao
interface FrameInfoDao {
    @Query ("SELECT * FROM frame_info_table ORDER BY number ASC")
    fun getAll() : List<FrameInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(frameInfo: FrameInfo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(frameInfo: FrameInfo)

    @Query ("DELETE FROM frame_info_table")
    fun deleteAll()
}

@Dao
interface RollInfoDao {
    @Query ("SELECT * FROM roll_info_table ORDER BY camera_name ASC")
    fun getAll() : List<RollInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rollInfo: RollInfo) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(rollInfo: RollInfo)

    @Query ("DELETE FROM roll_info_table")
    fun deleteAll()
}
// DAOs


@Database(entities = [CameraMaker::class, CameraModel::class,
                        FilmMaker::class, FilmModel::class,
                        FrameInfo::class, RollInfo::class], version = 1, exportSchema = false)
abstract class ExposureNotesDatabase : RoomDatabase() {
    abstract fun cameraMakerDao() : CameraMakerDao
    abstract fun cameraModelDao() : CameraModelDao

    abstract fun filmMakerDao() : FilmMakerDao
    abstract fun filmModelDao() : FilmModelDao

    abstract fun frameInfoDao() : FrameInfoDao

    abstract fun rollInfoDao() : RollInfoDao

}


