package com.brianmk.exposurenotes

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter


class MainActivity : AppCompatActivity() {

    private var frameDataList: MutableList<FrameData> = mutableListOf<FrameData>()

/*
            = listOf(
        FrameData(7, 5, "pretty trees"),
        FrameData(1, 8, "pretty flowers"),
        FrameData(6, 4, "pretty rocks"),
        FrameData(5, 2, "pretty mountains"),
        FrameData(3, 1, "pretty stars"),
        FrameData(10, 7, "pretty bikes"),
        FrameData(1, 6, "pretty lake"),
        FrameData(1, 4, "pretty faces"),
        FrameData(3, 5, "pretty dogs"),
        FrameData(6, 6, "pretty animals"),
        FrameData(5, 8, "grumpy cats"),
        FrameData(4, 2, "pretty stuff")
    )
*/
    private var frameArrayAdapter: FrameArrayAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check that we have permission to read/write on the filesystem
        verifyStoragePermissions(this)

        val myToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(myToolbar)

        val frameCount = 12

        // Create a list of blank exposure information
        for (i in 0 until frameCount) {
            frameDataList.add(i, FrameData())
        }


        // Create an adapter with the list data, attach that to the list view
        frameArrayAdapter = FrameArrayAdapter(this, frameDataList)
        val frameListView = findViewById<ListView>(R.id.frame_list)
        frameListView.adapter = frameArrayAdapter

        // Allow list touching
        frameListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            val args = Bundle()
            args.putInt("position", pos)
            args.putInt("shutter", frameDataList[pos].shutterIdx)
            args.putInt("aperture", frameDataList[pos].apertureIdx)
            args.putString("notes", frameDataList[pos].notes)


            val frameDialog = FrameInfoDialog()
            frameDialog.arguments = args

            val fm = supportFragmentManager
            frameDialog.show(fm, "Frame Dialog")
        }

        // Export button
        // TODO FIX THIS
        val exportButton = findViewById<Button>(R.id.export_button)
        exportButton.setOnClickListener {

        }
    } // mainActivity


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
            R.id.main_menu_camera -> setCamera()
            R.id.main_menu_film -> setFilm()
            R.id.main_menu_clear_roll -> Toast.makeText(applicationContext, "clear roll", Toast.LENGTH_LONG).show()
            else -> {
            }
        }// gets called when the sub-menu gets tapped
        //  (do nothing)

        return super.onOptionsItemSelected(item)
    } //onOptionsItemSelected()

    fun setCamera() {
        val cameraDialog = CameraSettingsDialog()
        cameraDialog.show(fragmentManager, null)

        saveRollInfo()
    }

    fun setFilm() {
        val filmDialog = FilmSettingsDialog()
        filmDialog.show(fragmentManager, null)

        saveRollInfo()

    }

    fun saveRollInfo() {

    }

    fun setSingleFrameData(pos: Int, t: Int, a: Int, n: String) {
        frameDataList[pos].shutterIdx = t
        frameDataList[pos].apertureIdx = a
        frameDataList[pos].notes = n
        frameDataList[pos].updateExposureSettings()

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
        private val LOG_TAG = MainActivity::class.java.getSimpleName()

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
