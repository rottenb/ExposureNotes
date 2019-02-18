package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        // TODO TEST DATA
        currentFilmRoll = FilmData(frames = 12)
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

        // Allow list touching, call a frame info dialog
        frameListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            frameSetDialog(pos)
        }

        // Export button
        // TODO FIX THIS
        val exportButton = findViewById<View>(R.id.export_button) as Button
        exportButton.setOnClickListener {
            Toast.makeText(this, "Works in theory, not in fact.", Toast.LENGTH_LONG).show()
        }
    } // mainActivity

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
            R.id.main_menu_film -> setFilmDialog()
            R.id.main_menu_clear_roll -> clearFilmRoll()
            R.id.main_menu_fill_with_junk -> fillFilmRollJunk()
            else -> {
                // do nothing
            }
        }

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

    // Fill frame list with random test data
    private fun fillFilmRollJunk() {
        fun getRandomString(length: Int) : String {
            val paragraph = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id" +
                    "feugiat metus. Fusce vulputate elit in consectetur hendrerit. Donec ut " +
                    "ullamcorper tortor. Fusce viverra justo a magna accumsan, at malesuada orci" +
                    "pharetra."

            var start = Random().nextInt(paragraph.length) - length
            if (start <= 0) {
                start = 1
            }

            return paragraph.substring(start, start + length )
        }

        for (i in 0 until currentFilmRoll.frames) {
            frameDataList[i].shutterIdx = Random().nextInt(12) + 1
            frameDataList[i].apertureIdx = Random().nextInt(20) + 1
            frameDataList[i].notes = getRandomString(Random().nextInt(20) + 1)
            frameDataList[i].lensIdx = Random().nextInt(6) + 1
            frameDataList[i].updateData()
        }

        frameArrayAdapter?.notifyDataSetInvalidated()
    } // fillFilmRollJunk

    private fun clearFilmRoll() {
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Clear all frame data?")
        alertBuilder.setPositiveButton("YES") { _, _ ->
            frameDataList.clear()

            for (i in 0 until currentFilmRoll.frames) {
                frameDataList.add(i, FrameData())
            }

            frameArrayAdapter?.notifyDataSetChanged()
        }
        alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

        val warnDialog: Dialog = alertBuilder.create()
        warnDialog.show()
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
        updateNotesHeader()
    } // setFilmData

    private fun updateNotesHeader() {
        var textView = findViewById<View>(R.id.camera_name) as TextView
        var str = "${currentCamera.manu} ${currentCamera.name}"
        textView.text = str

        textView = findViewById<View>(R.id.film) as TextView
        str = "${currentFilmRoll.manu} ${currentFilmRoll.name}"
        textView.text = str

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
        val obj = outJ.createJSONobj("Fuji GW690ii", "EBC Fujinon 90mm f/3.5",
                "Kodak Portra 160", "160", "0", frameDataList)
        try {
            val file = File("/storage/emulated/0/ExposureNotes/$filename")
            val output = BufferedWriter(FileWriter(file))
            output.write(obj.toString())
            output.close()
            Toast.makeText(applicationContext, "JSON saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            val error = "ERROR! Ensure that /storage/emulated/0/ExposureNotes is created and writeable"
            Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
            Log.d(LOG_TAG, "FILE WRITE!!")
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
