package com.brianmk.exposurenotes.data

import androidx.room.*

@Entity(tableName = "product_names_table")
data class ProductNamesTable(@PrimaryKey @ColumnInfo(name = "idx") val idx: Int,
                             @ColumnInfo(name = "camera_makers") val cameraMakers: String,
                             @ColumnInfo(name = "camera_models") val cameraModels: String,

                             @ColumnInfo(name = "lens_makers") val lensMakers: String,
                             @ColumnInfo(name = "lens_models") val lensModels: String,

                             @ColumnInfo(name = "film_makers") val filmMakers: String,
                             @ColumnInfo(name = "film_models") val filmModels: String)

@Entity(tableName = "frame_info_table")
data class FrameInfoTable(@PrimaryKey @ColumnInfo(name = "idx") val idx: Int,
                     @ColumnInfo(name = "shutterIdx") val shutterIdx: Int,
                     @ColumnInfo(name = "apertureIdx") val apertureIdx: Int,
                     @ColumnInfo(name = "lensIdx") val lensIdx: Int,
                     @ColumnInfo(name = "notes") val notes: String )

@Entity(tableName = "roll_info_table")
data class RollInfoTable(@PrimaryKey @ColumnInfo(name = "idx") val idx: Int,
                    @ColumnInfo(name = "camera_maker") val cameraMaker: String,
                    @ColumnInfo(name = "camera_name") val cameraName: String,
                    @ColumnInfo(name = "camera_format") val cameraFormat: String,
                    @ColumnInfo(name = "camera_lensIdx") val cameraLensIdx: Int,

                    @ColumnInfo(name = "film_maker") val filmMaker: String,
                    @ColumnInfo(name = "film_name") val filmName: String,
                    @ColumnInfo(name = "film_isoIdx") val filmIsoIdx: Int,
                    @ColumnInfo(name = "film_frames") val filmFrames: Int,
                    @ColumnInfo(name = "film_devIdx") val filmDevIdx: Int)
// Entities
@Dao
interface ProductNamesDao {
    @Query ("SELECT * FROM product_names_table ORDER BY idx ASC")
    fun getAll() : List<ProductNamesTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productNamesTable: ProductNamesTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(productNamesTable: ProductNamesTable)

    @Query ("DELETE FROM product_names_table")
    fun deleteAll()
}

@Dao
interface FrameInfoDao {
    @Query ("SELECT * FROM frame_info_table ORDER BY idx ASC")
    fun getAll() : List<FrameInfoTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(frameInfo: FrameInfoTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(frameInfo: FrameInfoTable)

    @Query ("DELETE FROM frame_info_table")
    fun deleteAll()
}

@Dao
interface RollInfoDao {
    @Query ("SELECT * FROM roll_info_table ORDER BY camera_name ASC")
    fun getAll() : List<RollInfoTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rollInfo: RollInfoTable) : Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(rollInfo: RollInfoTable)

    @Query ("DELETE FROM roll_info_table")
    fun deleteAll()
}
// DAOs


@Database(entities = [ProductNamesTable::class, FrameInfoTable::class, RollInfoTable::class], version = 1, exportSchema = false)
abstract class ExposureNotesDatabase : RoomDatabase() {
    abstract fun productNamesDao() : ProductNamesDao

    abstract fun frameInfoDao() : FrameInfoDao

    abstract fun rollInfoDao() : RollInfoDao

}


