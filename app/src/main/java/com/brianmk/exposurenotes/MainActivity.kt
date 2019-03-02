package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
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
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*


class MainActivity : AppCompatActivity() {
    private var frameDataList: MutableList<FrameData> = mutableListOf()
    private var frameArrayAdapter: FrameArrayAdapter? = null

    private lateinit var currentFilmRoll: FilmData
    private lateinit var currentCamera: CameraData

    //lateinit var cameraMakerDB: CameraMakerDatabase

    private lateinit var productNames: ProductNameArrays

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        currentFilmRoll = FilmData()
        currentCamera = CameraData()
        productNames = ProductNameArrays()

        //var cameraMakerDB = getDatabase(this)

        updateNotesHeader()

        // Create a list of blank exposure information
        for (i in 0 until currentFilmRoll.frames) {
            frameDataList.add(i, FrameData())
        }

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

        if (!cameraSet || !filmSet || currentFilmRoll.frames <= 0) {
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
        args.putString("camera", currentCamera.name)
        args.putString("film", currentFilmRoll.name)

        val exportDialog = ExportDialog()
        exportDialog.arguments = args

        val fm = supportFragmentManager
        exportDialog.show(fm, "Export Dialog")
    }

    // Set arguments to pass to the frame dialog, call the dialog
    private fun setFrameDialog(pos: Int) {
        val args = Bundle()

        // If not the first frame, and not blank, take defaults from previous
        if (pos != 0 && frameDataList[pos].shutterIdx == 0 && frameDataList[pos].apertureIdx == 0) {
            args.putInt("shutter", frameDataList[pos - 1].shutterIdx)
            args.putInt("aperture", frameDataList[pos - 1].apertureIdx)
            args.putInt("lens", frameDataList[pos - 1].lensIdx)
            args.putBoolean("fixed", currentCamera.fixed)
        } else {
            args.putInt("shutter", frameDataList[pos].shutterIdx)
            args.putInt("aperture", frameDataList[pos].apertureIdx)
            //args.putInt("lens", frameDataList[pos].lensIdx)
            args.putInt("lens", currentCamera.lensIdx)
            args.putBoolean("fixed", currentCamera.fixed)
        }

        // These are always unique to each frame
        args.putInt("position", pos)
        args.putString("notes", frameDataList[pos].notes)

        val frameDialog = FrameDialog()
        frameDialog.arguments = args

        val fm = supportFragmentManager
        frameDialog.show(fm, "Frame Dialog")
    } // frameSetDialog


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    } // onCreateOptionsMenu()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        // Menu items
        when (id) {
            R.id.main_menu_camera -> setCameraDialog()
            R.id.main_menu_lens -> setLensDialog()
            R.id.main_menu_film -> setFilmDialog()
            R.id.main_menu_clear_roll -> clearFilmRoll()
            R.id.main_menu_export_roll -> exportDialog()
            R.id.main_menu_junk -> fillWithTestData()
            R.id.main_menu_db -> testDB()
            else -> {
                // do nothing
            }
        }

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

    private fun testDB() {

    }

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
                str = str + paragraph[Random().nextInt(paragraph.size)]
            }

            return str
        }

        clearFilmRoll(false)
        currentFilmRoll.frames = Random().nextInt(36) + 1

        for (i in 0 until currentFilmRoll.frames) {
            frameDataList.add(i, FrameData())
        }

        for (i in 0 until currentFilmRoll.frames) {
            frameDataList[i].shutterIdx = Random().nextInt(12) + 1
            frameDataList[i].apertureIdx = Random().nextInt(20) + 1
            frameDataList[i].notes = getRandomString()
            frameDataList[i].lensIdx = Random().nextInt(6) + 1
            frameDataList[i].updateData()
        }

        currentCamera.manu = "Mamiya"
        currentCamera.name = "RB67sd"
        currentCamera.format = "120mm"

        currentFilmRoll.manu = "Ilford"
        currentFilmRoll.name = "HP5+"
        currentFilmRoll.isoIdx = Random().nextInt(12) + 1
        currentFilmRoll.devIdx = Random().nextInt(7) + 1
        currentFilmRoll.updateData()

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

                currentFilmRoll.frames = 0

                setListVisibility()

                frameArrayAdapter?.notifyDataSetChanged()

                currentCamera.clearData()
                currentFilmRoll.clearData()
                updateNotesHeader()
            }
            alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()
        } else {
            frameDataList.clear()

            currentFilmRoll.frames = 0

            setListVisibility()

            frameArrayAdapter?.notifyDataSetChanged()

            currentCamera.clearData()
            currentFilmRoll.clearData()
            updateNotesHeader()
        }


    } // clearFilmRoll

    private fun setCameraDialog() {
        val args = Bundle()
        args.putStringArray("makers", productNames.cameraMakers.toTypedArray())
        args.putStringArray("models", productNames.cameraModels.toTypedArray())
        args.putString("manu", currentCamera.manu)
        args.putString("name", currentCamera.name)
        args.putString("format", currentCamera.format)
        args.putInt("lens", currentCamera.lensIdx)
        args.putBoolean("fixed", currentCamera.fixed)

        val cameraDialog = CameraDialog()
        cameraDialog.arguments = args

        val fm = supportFragmentManager
        cameraDialog.show(fm, "Camera Dialog")
    } // setCameraDialog

    fun setCameraData(manu: String, name: String, format: String, lensIdx: Int,
                      isFixed: Boolean) {
        currentCamera.manu = manu
        currentCamera.name = name
        currentCamera.format = format
        currentCamera.lensIdx = lensIdx
        currentCamera.fixed = isFixed

        updateNotesHeader()
    } // setCameraData

    /*
        SET LENS DIALOG
     */
    private fun setLensDialog() {
        val lensDialog = LensDialog()
        val fm = supportFragmentManager
        lensDialog.show(fm, "Lens Dialog")
    } // setLensDialog


    private fun setFilmDialog() {
        val args = Bundle()
        args.putStringArray("makers", productNames.filmMakers.toTypedArray())
        args.putStringArray("models", productNames.filmModels.toTypedArray())
        args.putString("manu", currentFilmRoll.manu)
        args.putString("name", currentFilmRoll.name)
        args.putInt("iso", currentFilmRoll.isoIdx)
        args.putInt("frames", currentFilmRoll.frames)
        args.putInt("dev", currentFilmRoll.devIdx)
        args.putString("notes", currentFilmRoll.notes)

        val filmDialog = FilmDialog()
        filmDialog.arguments = args

        val fm = supportFragmentManager
        filmDialog.show(fm, "Film Dialog")
    } // setFilmDialog

    fun setFilmData(manu: String, name: String, iso: Int, frames: Int, dev: Int, notes: String) {
        updateFrameCount(frames)

        currentFilmRoll.manu = manu
        currentFilmRoll.name = name
        currentFilmRoll.frames = frames
        currentFilmRoll.isoIdx = iso
        currentFilmRoll.devIdx = dev
        currentFilmRoll.notes = notes
        currentFilmRoll.updateData()

        setListVisibility()
        updateNotesHeader()
    } // setFilmData

    private fun updateFrameCount(frames: Int) {
        // Change the frame list to reflect the new size
        // If new film is larger, add frames.
        // If new film is smaller, remove frames from the bottom
        if (currentFilmRoll.frames < frames) {
            for(i in currentFilmRoll.frames until frames) {
                frameDataList.add(i, FrameData())
            }
        } else if (currentFilmRoll.frames > frames) {
            for(i in currentFilmRoll.frames - 1 downTo frames) {
                frameDataList.removeAt(i)
            }
        }

        frameArrayAdapter?.notifyDataSetInvalidated()

    }

    private fun updateNotesHeader() {
        var textView = findViewById<View>(R.id.camera_name) as TextView
        var str = "${currentCamera.manu} ${currentCamera.name}"
        textView.text = str

        textView = findViewById<View>(R.id.film_name) as TextView
        str = "${currentFilmRoll.manu} ${currentFilmRoll.name}"
        textView.text = str

        textView = findViewById<View>(R.id.format) as TextView
        textView.text = currentCamera.format

        textView = findViewById<View>(R.id.iso) as TextView
        textView.text = currentFilmRoll.iso

        textView = findViewById<View>(R.id.dev) as TextView
        textView.text = currentFilmRoll.dev

        setListVisibility()
    } // updateNotesHeader

    fun saveRollInfo() {

    } // saveRollInfo

    fun setSingleFrameData(pos: Int, shutter: Int, aperture: Int, lens: Int, notes: String) {
        frameDataList[pos].shutterIdx = shutter
        frameDataList[pos].apertureIdx = aperture
        frameDataList[pos].lensIdx = lens
        frameDataList[pos].notes = notes
        frameDataList[pos].updateData()

        frameArrayAdapter!!.notifyDataSetChanged()
    } // setSingleFrameData

    fun exportFilmRoll(filename: String, method: String) {
        val outJ = OutputJSON()
        val obj = outJ.createJSONobj(currentCamera.name, currentFilmRoll.name, currentFilmRoll.iso, currentFilmRoll.dev, frameDataList)
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

        @Volatile
        private var INSTANCE: CameraMakerDatabase? = null

        fun getDatabase(context: Context) : CameraMakerDatabase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    CameraMakerDatabase::class.java,
                                                    "camera_maker_database").build()
                INSTANCE = instance
                return instance
            }
        }

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
        } // verifyStoragePermisions
    }

}
