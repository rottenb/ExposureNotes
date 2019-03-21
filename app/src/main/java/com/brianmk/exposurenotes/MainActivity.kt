package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.brianmk.exposurenotes.adapter.FrameArrayAdapter
import com.brianmk.exposurenotes.data.*
import com.brianmk.exposurenotes.dialog.*
import com.brianmk.exposurenotes.util.OutputJSON
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*


class MainActivity : AppCompatActivity() {
    private var frameDataList: MutableList<FrameData> = mutableListOf()
    private var frameArrayAdapter: FrameArrayAdapter? = null

    private lateinit var currentFilm: FilmData
    private lateinit var currentCamera: CameraData

    lateinit var exposureDB: ExposureNotesDatabase

    private lateinit var productNames: ProductNames

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        currentFilm = FilmData()
        currentCamera = CameraData()
        productNames = ProductNames()
        productNames.lensModels = productNames.lensModels.sorted() as MutableList<String>

        exposureDB = Room.databaseBuilder(applicationContext,
                                    ExposureNotesDatabase::class.java,
                              "exposure_notes_db").fallbackToDestructiveMigration().build()

        doAsync {
            // If there's a frame info to load, do that
            //  Otherwise load an empty list
            if (exposureDB.rollInfoDao().getAll().isNotEmpty()) {
                loadDatabase()
            } else {
                for (i in 0 until currentFilm.frames) {
                    frameDataList.add(i, FrameData())
                }
            }

            if (exposureDB.productNamesDao().getAll().isNotEmpty()) {
                val products = exposureDB.productNamesDao().getAll()[0]
                productNames.cameraMakers = products.cameraMakers.split("•") as MutableList<String>
                productNames.cameraModels = products.cameraModels.split("•") as MutableList<String>
                productNames.lensMakers = products.lensMakers.split("•") as MutableList<String>
                productNames.lensModels = products.lensModels.split("•") as MutableList<String>
                productNames.filmMakers = products.filmMakers.split("•") as MutableList<String>
                productNames.filmModels = products.filmModels.split("•") as MutableList<String>
            }
        }

        updateNotesHeader()

        // Create an adapter with the list data, attach that to the list view
        frameArrayAdapter = FrameArrayAdapter(this, frameDataList)
        val frameListView = findViewById<View>(R.id.frame_list) as ListView
        frameListView.adapter = frameArrayAdapter

        setListVisibility()

        findViewById<View>(R.id.camera_setup).setOnClickListener {
            if ((findViewById<View>(R.id.camera_name) as TextView).text.toString() == " " ) {
                setCameraDialog()
            }
        }

        findViewById<View>(R.id.film_setup).setOnClickListener {
            if ((findViewById<View>(R.id.film_name) as TextView).text.toString() == " " ) {
                setFilmDialog()
            }
        }
/*
        findViewById<View>(R.id.roll_settings).setOnClickListener {
            val quickDialog = QuickSettingsDialog()

            val fm = supportFragmentManager
            quickDialog.show(fm, "Quick Settings Dialog")        }
*/
        frameListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            setFrameDialog(pos)
        }

    } // onCreate

    private fun setListVisibility() {
        var cameraSet = false
        if ((findViewById<View>(R.id.camera_name) as TextView).text != " ") {
            (findViewById<View>(R.id.camera_setup) as ImageView).setColorFilter(getColor(R.color.light_grey))
            (findViewById<View>(R.id.camera_setup_text) as TextView).setTextColor(getColor(R.color.light_grey))
            cameraSet = true
        } else {
            (findViewById<View>(R.id.camera_setup) as ImageView).setColorFilter(getColor(R.color.colorPrimary))
            (findViewById<View>(R.id.camera_setup_text) as TextView).setTextColor(getColor(R.color.colorPrimary))
        }

        var filmSet = false
        if ((findViewById<View>(R.id.film_name) as TextView).text != " ") {
            (findViewById<View>(R.id.film_setup) as ImageView).setColorFilter(getColor(R.color.light_grey))
            (findViewById<View>(R.id.film_setup_text) as TextView).setTextColor(getColor(R.color.light_grey))
            filmSet = true
        } else {
            (findViewById<View>(R.id.film_setup) as ImageView).setColorFilter(getColor(R.color.colorPrimary))
            (findViewById<View>(R.id.film_setup_text) as TextView).setTextColor(getColor(R.color.colorPrimary))
        }

        if (!cameraSet || !filmSet || currentFilm.frames <= 0) {
            findViewById<View>(R.id.frame_list).visibility = View.INVISIBLE
            findViewById<View>(R.id.list_end_bar).visibility = View.INVISIBLE
            findViewById<View>(R.id.blank_list).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.blank_list).visibility = View.GONE
            findViewById<View>(R.id.frame_list).visibility = View.VISIBLE
            findViewById<View>(R.id.list_end_bar).visibility = View.VISIBLE
        }
    } // setListVisibility

    // Export the film roll information
    private fun exportDialog() {
        val args = Bundle()
        args.putString("camera", currentCamera.model)
        args.putString("film", currentFilm.model)

        val exportDialog = ExportDialog()
        exportDialog.arguments = args

        val fm = supportFragmentManager
        exportDialog.show(fm, "Export Dialog")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    } // onCreateOptionsMenu()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Menu items
        when (item.itemId) {
            R.id.main_menu_about -> aboutDialog()
            R.id.main_menu_camera -> setCameraDialog()
            R.id.main_menu_lens -> setLensDialog()
            R.id.main_menu_film -> setFilmDialog()
            R.id.main_menu_clear_roll -> clearFilmRoll()
            R.id.main_menu_export_roll -> exportDialog()
            R.id.main_menu_junk -> fillWithTestData()
            R.id.database_menu_save -> saveDatabase()
            R.id.database_menu_load -> loadDatabase()
            R.id.database_menu_delete -> deleteDB()
            R.id.database_menu_test -> testDB()
        }

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

    private fun saveDatabase(showMsg: Boolean = true) {
        doAsync {
            val roll = RollInfoTable(0, currentCamera.maker, currentCamera.model, currentCamera.format, currentCamera.lensIdx,
                    currentFilm.maker, currentFilm.model, currentFilm.isoIdx, currentFilm.frames, currentFilm.devIdx)
            if (exposureDB.rollInfoDao().getAll().isEmpty()) {
                exposureDB.rollInfoDao().insert(roll)
            } else {
                exposureDB.rollInfoDao().update(roll)
            }

            for (i in 0 until currentFilm.frames) {
                val frame = FrameInfoTable(i, frameDataList[i].shutterIdx, frameDataList[i].apertureIdx, frameDataList[i].lensIdx, frameDataList[i].notes)

                exposureDB.frameInfoDao().insert(frame)
            }

            val products = ProductNamesTable(0, productNames.cameraMakers.joinToString(separator = "•"),
                                                     productNames.cameraModels.joinToString(separator = "•"),
                                                     productNames.lensMakers.joinToString(separator = "•"),
                                                     productNames.lensModels.joinToString(separator = "•"),
                                                     productNames.filmMakers.joinToString(separator = "•"),
                                                     productNames.filmModels.joinToString(separator = "•"))
            if (exposureDB.productNamesDao().getAll().isEmpty()) {
                exposureDB.productNamesDao().insert(products)
            } else {
                exposureDB.productNamesDao().update(products)
            }

            uiThread {
                if (showMsg) {
                    toast("Saved")
                }
            }
        }
    }

    private fun loadDatabase(showMsg: Boolean = true) {
        doAsync {
            val roll = exposureDB.rollInfoDao().getAll()
            val frames = exposureDB.frameInfoDao().getAll()

            val products = exposureDB.productNamesDao().getAll()[0]
            productNames.cameraMakers = products.cameraMakers.split("•") as MutableList<String>
            productNames.cameraModels = products.cameraModels.split("•") as MutableList<String>
            productNames.lensMakers = products.lensMakers.split("•") as MutableList<String>
            productNames.lensModels = products.lensModels.split("•") as MutableList<String>
            productNames.filmMakers = products.filmMakers.split("•") as MutableList<String>
            productNames.filmModels = products.filmModels.split("•") as MutableList<String>

            uiThread {
                if (roll.isNotEmpty()) {
                    setCameraData(roll[0].cameraMaker, roll[0].cameraName,roll[0].cameraFormat, roll[0].cameraLensIdx)
                    setFilmData(roll[0].filmMaker, roll[0].filmName, roll[0].filmIsoIdx, roll[0].filmFrames, roll[0].filmDevIdx)

                    updateNotesHeader()
                    setListVisibility()

                    frameDataList.clear()
                    for (i in 0 until currentFilm.frames) {
                        frameDataList.add(i, FrameData(frames[i].shutterIdx, frames[i].apertureIdx, frames[i].lensIdx, frames[i].notes))
                        frameDataList[i].updateData()
                    }
                }

                if (showMsg) {
                    toast("Loaded")
                }

            }
        }


    }

    private fun deleteDB() {
        doAsync {
            exposureDB.productNamesDao().deleteAll()
            exposureDB.frameInfoDao().deleteAll()
            exposureDB.rollInfoDao().deleteAll()
        }

        this.deleteDatabase("exposure_notes_db")
        exposureDB = Room.databaseBuilder(applicationContext,
                ExposureNotesDatabase::class.java,
                "exposure_notes_db").fallbackToDestructiveMigration().build()

        clearFilmRoll(false)
    }

    private fun testDB() {
        doAsync {


            uiThread {
            }

        }
    } // testDB

    // Fill frame list with random test data
    private fun fillWithTestData() {
        fun getRandomString() : String {
            val paragraph = arrayOf("Lorem ", "ipsum ", "dolor ", "sit ", "amet ", "consectetur ",
                    "adipiscing ", "elit ", "Nulla ",  "id ", "feugiat ", "metus ", "Fusce ", "vulputate ",
                    "elit ", "in ", "consectetur ", "hendrerit ", "Donec ", "ut ", "ullamcorper ", "tortor ",
                    "Fusce ", "viverra ", "justo ", "a ", "magna ", "accumsan ", "at ", "malesuada ", "orci ",
                    "pharetra. ")

            var str = ""
            val limit = Random().nextInt(4) + 2
            for (i in 1 until limit) {
                str += paragraph[Random().nextInt(paragraph.size)]
            }

            return str
        }

        clearFilmRoll(false)
        currentFilm.frames = Random().nextInt(36) + 1

        for (i in 0 until currentFilm.frames) {
            frameDataList.add(i, FrameData(Random().nextInt(5) + 1,
                                           Random().nextInt(5 + 1),
                                           Random().nextInt(productNames.lensModels.size),
                                           getRandomString() ))
        }

        currentCamera.maker = "Mamiya"
        currentCamera.model = "RB67sd"
        currentCamera.format = "120mm"

        currentFilm.maker = "Ilford"
        currentFilm.model = "HP5+"
        currentFilm.isoIdx = Random().nextInt(12) + 1
        currentFilm.devIdx = Random().nextInt(7) + 1
        currentFilm.updateData()

        updateNotesHeader()
        setListVisibility()

        frameArrayAdapter?.notifyDataSetInvalidated()
    } // fillFilmRollJunk

    private fun clearFilmRoll(showWarning: Boolean = true) {
        if (showWarning) {
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setMessage("Clear all frame data?")
            alertBuilder.setPositiveButton("YES") { _, _ ->
                frameDataList.clear()
                currentFilm.frames = 0

                setListVisibility()

                frameArrayAdapter?.notifyDataSetChanged()

                currentCamera.clearData()
                currentFilm.clearData()
                updateNotesHeader()
            }
            alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()
        } else {
            frameDataList.clear()

            currentFilm.frames = 0

            setListVisibility()

            frameArrayAdapter?.notifyDataSetChanged()

            currentCamera.clearData()
            currentFilm.clearData()
            updateNotesHeader()
        }
    } // clearFilmRoll

    private fun aboutDialog() {
        val aboutDialog = AboutDialog()
        val fm = supportFragmentManager
        aboutDialog.show(fm, "About Dialog")
    } // aboutDialog

    // Set arguments to pass to the frame dialog, call the dialog
    private fun setFrameDialog(pos: Int) {
        val args = Bundle()

        // If not the first frame, and not blank, take defaults from previous
        if (pos != 0 && frameDataList[pos].shutterIdx == 0 && frameDataList[pos].apertureIdx == 0) {
            args.putInt("shutter", frameDataList[pos - 1].shutterIdx)
            args.putInt("aperture", frameDataList[pos - 1].apertureIdx)
            args.putInt("lensIdx", frameDataList[pos - 1].lensIdx)
        } else if (pos == 0) {
            args.putInt("shutter", frameDataList[pos].shutterIdx)
            args.putInt("aperture", frameDataList[pos].apertureIdx)
            args.putInt("lensIdx", currentCamera.lensIdx)
        } else {
            args.putInt("shutter", frameDataList[pos].shutterIdx)
            args.putInt("aperture", frameDataList[pos].apertureIdx)
            args.putInt("lensIdx", frameDataList[pos].lensIdx)
        }

        // These are always unique to each frame
        args.putInt("position", pos)
        args.putString("notes", frameDataList[pos].notes)

        args.putStringArray("lenses", productNames.lensModels.toTypedArray())

        val frameDialog = FrameDialog()
        frameDialog.arguments = args

        val fm = supportFragmentManager
        frameDialog.show(fm, "Frame Dialog")
    } // frameSetDialog

    fun setFrameData(pos: Int, shutter: Int, aperture: Int, lensIdx: Int, notes: String) {
        frameDataList[pos].shutterIdx = shutter
        frameDataList[pos].apertureIdx = aperture
        frameDataList[pos].lensIdx = lensIdx
        currentCamera.lensIdx = lensIdx
        frameDataList[pos].notes = notes
        frameDataList[pos].updateData()

        frameArrayAdapter!!.notifyDataSetChanged()

        // Auto-save frame data?
        saveDatabase(true)
    } // setFrameData

    private fun setCameraDialog() {
        val args = Bundle()
        args.putStringArray("makers", productNames.cameraMakers.toTypedArray())
        args.putStringArray("models", productNames.cameraModels.toTypedArray())
        args.putStringArray("lens_makers", productNames.lensMakers.toTypedArray())
        args.putStringArray("lenses", productNames.lensModels.toTypedArray())
        args.putString("maker", currentCamera.maker)
        args.putString("model", currentCamera.model)
        args.putString("format", currentCamera.format)
        args.putInt("lensIdx", currentCamera.lensIdx)

        val cameraDialog = CameraDialog()
        cameraDialog.arguments = args

        val fm = supportFragmentManager
        cameraDialog.show(fm, "Camera Dialog")
    } // setCameraDialog

    fun setCameraData(manu: String, model: String, format: String, lensIdx: Int) {
        currentCamera.maker = manu
        currentCamera.model = model
        currentCamera.format = format
        currentCamera.lensIdx = lensIdx

        if (!productNames.cameraMakers.contains(manu)) {
            productNames.cameraMakers.add(manu)
        }

        if (!productNames.cameraModels.contains(model)) {
            productNames.cameraModels.add(model)
        }

        saveDatabase()
        updateNotesHeader()
    } // setCameraData

    /*
        SET LENS DIALOG
     */
    fun setLensDialog() {
        val args = Bundle()
        args.putStringArray("makers", productNames.lensMakers.toTypedArray())
        args.putStringArray("lenses", productNames.lensModels.toTypedArray())

        val lensDialog = LensDialog()
        lensDialog.arguments = args

        val fm = supportFragmentManager
        lensDialog.show(fm, "Lens Dialog")
    } // setLensDialog

    fun saveLensData(maker: String, model: String, delete: Boolean = false, lensIdx: Int = 0) {
        val modelString = "$maker $model"

        if (delete) {
            productNames.lensModels.removeAt(lensIdx)

            for (i in 0 until frameDataList.size) {
                val ldx = frameDataList[i].lensIdx
                if (ldx == lensIdx || ldx < 0 || ldx >= frameDataList.size) {
                    frameDataList[i].lensIdx = 0
                }
            }

        } else {
            if (!productNames.lensMakers.contains(maker)) {
                productNames.lensMakers.add(maker)
            }

            if (lensIdx > -1) {
                // Replace old lens description with edited one
                productNames.lensModels[lensIdx] = modelString
            } else {
                if (!productNames.lensModels.contains(modelString)) {
                    productNames.lensModels.add(modelString)
                }
            }
        }

        productNames.lensModels.sort()

        saveDatabase()
    } // saveLensData

    private fun setFilmDialog() {
        val args = Bundle()
        args.putStringArray("makers", productNames.filmMakers.toTypedArray())
        args.putStringArray("models", productNames.filmModels.toTypedArray())
        args.putString("maker", currentFilm.maker)
        args.putString("model", currentFilm.model)
        args.putInt("iso", currentFilm.isoIdx)
        args.putInt("frames", currentFilm.frames)
        args.putInt("dev", currentFilm.devIdx)

        val filmDialog = FilmDialog()
        filmDialog.arguments = args

        val fm = supportFragmentManager
        filmDialog.show(fm, "Film Dialog")
    } // setFilmDialog

    fun setFilmData(maker: String, model: String, iso: Int, frames: Int, dev: Int) {
        updateFrameCount(frames)

        currentFilm.maker = maker
        currentFilm.model = model
        currentFilm.frames = frames
        currentFilm.isoIdx = iso
        currentFilm.devIdx = dev
        currentFilm.updateData()

        if (!productNames.filmMakers.contains(maker)) {
            productNames.filmMakers.add(maker)
        }

        if (!productNames.filmModels.contains(model)) {
            productNames.filmModels.add(model)
        }

        saveDatabase()
        updateNotesHeader()
    } // setFilmData

    private fun updateFrameCount(frames: Int) {
        // Change the frame list to reflect the new size
        // If new film is larger, add frames.
        // If new film is smaller, remove frames from the bottom
        if (currentFilm.frames < frames) {
            for(i in currentFilm.frames until frames) {
                frameDataList.add(i, FrameData())
            }
        } else if (currentFilm.frames > frames) {
            for(i in currentFilm.frames - 1 downTo frames) {
                frameDataList.removeAt(i)
            }
        }

        frameArrayAdapter?.notifyDataSetInvalidated()

    }

    private fun updateNotesHeader() {
        var textView = findViewById<View>(R.id.camera_name) as TextView
        var str = "${currentCamera.maker} ${currentCamera.model}"
        textView.text = str

        textView = findViewById<View>(R.id.film_name) as TextView
        str = "${currentFilm.maker} ${currentFilm.model}"
        textView.text = str

        textView = findViewById<View>(R.id.format) as TextView
        textView.text = currentCamera.format

        textView = findViewById<View>(R.id.iso) as TextView
        textView.text = currentFilm.iso

        textView = findViewById<View>(R.id.dev) as TextView
        textView.text = currentFilm.dev

        setListVisibility()
    } // updateNotesHeader

    fun exportFilmRoll(filename: String, method: String) {
        val outJ = OutputJSON()
        val obj = outJ.createJSONobj(currentCamera.model, currentFilm.model, currentFilm.iso, currentFilm.dev, frameDataList)
        try {
            val filepath = File("storage/emulated/0/ExposureNotes/")
            filepath.mkdirs()

            val file = File("$filepath/$filename")
            val output = BufferedWriter(FileWriter(file))

            output.write(obj.toString())
            output.close()

            val msg: Toast = Toast.makeText(applicationContext, "JSON saved", Toast.LENGTH_SHORT)
            msg.setGravity(Gravity.CENTER, 0, 0)
            msg.show()

        } catch (e: Exception) {
            val error = "ERROR! Ensure that /storage/emulated/0/ExposureNotes is created and writable."
            val msg: Toast = Toast.makeText(applicationContext, error, Toast.LENGTH_LONG)
            msg.setGravity(Gravity.CENTER, 0, 0)
            msg.show()

            e.printStackTrace()
        }

    } // exportFilmRoll

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName

        // Storage Permissions
        private val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)


        // Check that the correct permissions are enabled, ask to enable if not
        fun verifyStoragePermissions(activity: Activity) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                )
            }
        } // verifyStoragePermissions
    }

}
