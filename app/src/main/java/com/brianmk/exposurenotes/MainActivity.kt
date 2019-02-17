package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
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


class MainActivity : AppCompatActivity() {

    private var frameDataList: MutableList<FrameData> = mutableListOf()
    private var frameArrayAdapter: FrameArrayAdapter? = null
    private lateinit var currentFilmRoll: FilmData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        // TODO TEST DATA
        currentFilmRoll = FilmData("Fujifilm", "Pro400H", 1,8, 12, 4)

        // Create a list of blank exposure information
        for (i in 0 until currentFilmRoll.frames) {
            frameDataList.add(i, FrameData())
        }

        // TODO TEST DATA
        frameDataList[0].shutterIdx = 1
        frameDataList[0].apertureIdx = 14
        frameDataList[0].notes = "Description"
        frameDataList[0].lensIdx = 5
        frameDataList[0].updateSettings()

        frameDataList[1].shutterIdx = 5
        frameDataList[1].apertureIdx = 1
        frameDataList[1].notes = "This description is too long!"
        frameDataList[1].lensIdx = 5
        frameDataList[1].updateSettings()

        frameDataList[2].shutterIdx = 7
        frameDataList[2].apertureIdx = 5
        frameDataList[2].notes = "Some sorta nonsense"
        frameDataList[2].lensIdx = 6
        frameDataList[2].updateSettings()

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
            setFilmDialog()
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
    }


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
            R.id.main_menu_clear_roll -> Toast.makeText(applicationContext, "clear roll", Toast.LENGTH_LONG).show()
            else -> {
                // do nothing
            }
        }

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

    fun setCameraDialog() {

    }

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
    }

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

        frameArrayAdapter?.notifyDataSetInvalidated()

        currentFilmRoll.manu = manu
        currentFilmRoll.name = name
        currentFilmRoll.formatIdx = format
        currentFilmRoll.isoIdx = iso
        currentFilmRoll.frames = frames
        currentFilmRoll.devIdx = dev
        currentFilmRoll.notes = notes
        currentFilmRoll.updateSettings()

        // Update Film Notes header
        var textView = findViewById<View>(R.id.film) as TextView
        val filmString = "${currentFilmRoll.manu} ${currentFilmRoll.name}"
        textView.text = filmString

        textView = findViewById<View>(R.id.iso) as TextView
        textView.text = currentFilmRoll.iso

        textView = findViewById<View>(R.id.dev) as TextView
        textView.text = currentFilmRoll.dev

    }

    fun saveRollInfo() {

    }

    fun setSingleFrameData(pos: Int, shutter: Int, aperture: Int, lens: Int, notes: String) {
        frameDataList[pos].shutterIdx = shutter
        frameDataList[pos].apertureIdx = aperture
        frameDataList[pos].lensIdx = lens
        frameDataList[pos].notes = notes
        frameDataList[pos].updateSettings()

        frameArrayAdapter!!.notifyDataSetChanged()
    }

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

    }

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
        }
    }

}
