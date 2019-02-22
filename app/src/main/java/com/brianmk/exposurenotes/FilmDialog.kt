package com.brianmk.exposurenotes

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FilmDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.film_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val manuText = rootView.findViewById<View>(R.id.manu_edit) as TextView
        manuText.text = arguments!!.getString("manu")

        val filmText = rootView.findViewById<View>(R.id.film_edit) as TextView
        filmText.text = arguments!!.getString("name")

        val frameText = rootView.findViewById<View>(R.id.frame_count) as TextView
        frameText.text = arguments!!.getInt("frames").toString()


        val isoSpin = rootView.findViewById<View>(R.id.iso_spinner) as Spinner
        val isoAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.isos, R.layout.spinner_item)
        isoSpin.adapter = isoAdapter
        isoSpin.setSelection(arguments!!.getInt("iso"))

        val devSpin = rootView.findViewById<View>(R.id.dev_spinner) as Spinner
        val devAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.devs, R.layout.spinner_item)
        devSpin.adapter = devAdapter
        devSpin.setSelection(arguments!!.getInt("dev"))

        val notesText = rootView.findViewById<View>(R.id.notes_edit) as TextView
        notesText.text = arguments!!.getString("notes")

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            //val frameCount = frames[formatSpin.selectedItemPosition]
            // This gets called if the user wants to truncate the film list
            val frameCount = frameText.text.toString().toInt()
            fun saveData() {
                (activity as MainActivity).setFilmData(
                        manuText.text.toString(),
                        filmText.text.toString(),
                        isoSpin.selectedItemPosition,
                        frameCount,
                        devSpin.selectedItemPosition,
                        notesText.text.toString() )
                dismiss()
            }

            // Warn if new frames size is smaller than old
            // Ask user if they'd like to proceed or whatever
            if (frameCount < arguments!!.getInt("frames")) {
                val alertBuilder = AlertDialog.Builder(rootView.context)
                alertBuilder.setMessage("New frame count is less than the current frame count.\n\n" +
                        "The frame list will be truncated and data will be lost!\n\nProceed?")
                alertBuilder.setPositiveButton("YES") { _, _ -> saveData() }
                alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

                val warnDialog: Dialog = alertBuilder.create()
                warnDialog.show()
            } else {
                saveData()
            }
        }

        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button
        cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    companion object {
        private val LOG_TAG = FilmDialog::class.java.simpleName
    }
}
