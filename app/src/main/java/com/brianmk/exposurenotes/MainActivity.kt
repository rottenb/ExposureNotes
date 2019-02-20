package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*


class MainActivity : AppCompatActivity() {
    private var frameDataList: MutableList<FrameData> = mutableListOf()
    private var frameArrayAdapter: FrameArrayAdapter? = null

    private lateinit var currentFilmRoll: FilmData
    private lateinit var currentCamera: CameraData

    private var frameCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        // TODO TEST DATA
        currentFilmRoll = FilmData()

        currentCamera = CameraData()
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

        // Allow list touching, call a frame info dialog
        findViewById<View>(R.id.blank_list).setOnClickListener {
            fillWithTestData() }

        frameListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            frameSetDialog(pos)
        }

    } // mainActivity

    private fun setListVisibility() {
        if (currentFilmRoll.frames <= 0) {
            findViewById<View>(R.id.frame_list).visibility = View.INVISIBLE
            findViewById<View>(R.id.list_end_bar).visibility = View.INVISIBLE
            findViewById<View>(R.id.blank_list).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.blank_list).visibility = View.GONE
            findViewById<View>(R.id.frame_list).visibility = View.VISIBLE
            findViewById<View>(R.id.list_end_bar).visibility = View.VISIBLE
        }
    }

    // Export the film roll information
    private fun exportRoll() {
        val args = Bundle()
        args.putString("camera", currentCamera.name)
        args.putString("film", currentFilmRoll.name)

        val exportDialog = ExportDialog()
        exportDialog.arguments = args
        val fm = supportFragmentManager
        exportDialog.show(fm, "Export Dialog")
    }

    // Set arguments to pass to the frame dialog, call the dialog
    private fun frameSetDialog(pos: Int) {
        val args = Bundle()

        // If not the first frame, and not blank, take defaults from previous
        if (pos != 0 && frameDataList[pos].shutterIdx == 0 && frameDataList[pos].apertureIdx == 0) {
            args.putInt("shutter", frameDataList[pos - 1].shutterIdx)
            args.putInt("aperture", frameDataList[pos - 1].apertureIdx)
            args.putInt("lens", frameDataList[pos - 1].lensIdx)
        } else {
            args.putInt("shutter", frameDataList[pos].shutterIdx)
            args.putInt("aperture", frameDataList[pos].apertureIdx)
            args.putInt("lens", frameDataList[pos].lensIdx)
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
            R.id.main_menu_export_roll -> exportRoll()
            R.id.main_menu_junk -> fillWithTestData()
            else -> {
                // do nothing
            }
        }

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

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
        currentCamera.formatIdx = 0
        currentCamera.updateData()

        currentFilmRoll.manu = "Ilford"
        currentFilmRoll.name = "HP5+"
        currentFilmRoll.isoIdx = Random().nextInt(12) + 1
        currentFilmRoll.devIdx = Random().nextInt(7) + 1
        currentFilmRoll.formatIdx = Random().nextInt(11) + 1
        currentFilmRoll.updateData()

        Log.d(LOG_TAG, "frames: ${currentFilmRoll.frames}")

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
            }
            alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()
        } else {
            frameDataList.clear()
            currentFilmRoll.frames = 0
            setListVisibility()
            frameArrayAdapter?.notifyDataSetChanged()
        }
    } // clearFilmRoll

    private fun setCameraDialog() {
        val args = Bundle()
        args.putString("manu", currentCamera.manu)
        args.putString("name", currentCamera.name)
        args.putInt("format", currentCamera.formatIdx)
        val cameraDialog = CameraDialog()
        cameraDialog.arguments = args

        val fm = supportFragmentManager
        cameraDialog.show(fm, "Camera Dialog")
    } // setCameraDialog

    private fun setLensDialog() {
        val lensDialog = LensDialog()
        val fm = supportFragmentManager
        lensDialog.show(fm, "Lens Dialog")
    }

    fun setCameraData(manu: String, name: String) {
        currentCamera.manu = manu
        currentCamera.name = name

        updateNotesHeader()
    } // setCameraData

    private fun setFilmDialog() {
        val args = Bundle()

        args.putString("manu", currentFilmRoll.manu)
        args.putString("name", currentFilmRoll.name)
        args.putInt("format", currentFilmRoll.formatIdx)
        args.putInt("iso", currentFilmRoll.isoIdx)
        args.putInt("frames", currentFilmRoll.frames)
        args.putInt("dev", currentFilmRoll.devIdx)
        args.putString("notes", currentFilmRoll.notes)

        val filmDialog = FilmDialog()
        filmDialog.arguments = args

        val fm = supportFragmentManager
        filmDialog.show(fm, "Film Dialog")
    } // setFilmDialog

    fun setFilmData(manu: String, name: String, format: Int, iso: Int, frames: Int, dev: Int, notes: String) {
        // Change the frame list to reflect the new size
        // If new film is larger, add frames.
        // If new film is smaller, remove frames from the bottom
        if (currentFilmRoll.frames < frames) {
            for(i in currentFilmRoll.frames until frames) {
                frameDataList.add(i, FrameData())
            }
        } else if (currentFilmRoll.frames > frames) {
            for(i in currentFilmRoll.frames - 1 downTo frames) {
                // TODO Post warning about cutting the list
                frameDataList.removeAt(i)
            }
        }

        currentFilmRoll.manu = manu
        currentFilmRoll.name = name
        currentFilmRoll.formatIdx = format
        currentFilmRoll.isoIdx = iso
        currentFilmRoll.frames = frames
        currentFilmRoll.devIdx = dev
        currentFilmRoll.notes = notes
        currentFilmRoll.updateData()

        frameArrayAdapter?.notifyDataSetInvalidated()
        setListVisibility()
        updateNotesHeader()
    } // setFilmData

    private fun updateNotesHeader() {
        var textView = findViewById<View>(R.id.camera_name) as TextView
        var str = "${currentCamera.manu} ${currentCamera.name}"
        textView.text = str

        textView = findViewById<View>(R.id.film) as TextView
        str = "${currentFilmRoll.manu} ${currentFilmRoll.name}"
        textView.text = str

        textView = findViewById<View>(R.id.format) as TextView
        textView.text = currentFilmRoll.format

        textView = findViewById<View>(R.id.iso) as TextView
        textView.text = currentFilmRoll.iso

        textView = findViewById<View>(R.id.dev) as TextView
        textView.text = currentFilmRoll.dev
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
            Log.d(LOG_TAG, "$filepath/$filename")
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
        } // verifyStoragePermisions
    }

}
