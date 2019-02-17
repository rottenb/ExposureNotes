package com.brianmk.exposurenotes

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment

class FilmDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.film_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val manuText = rootView.findViewById<View>(R.id.manu_edit) as TextView
        manuText.text = arguments!!.getString("manu")

        val filmText = rootView.findViewById<View>(R.id.film_edit) as TextView
        filmText.text = arguments!!.getString("name")

        val formatSpin = rootView.findViewById<View>(R.id.format_spinner) as Spinner
        val formatAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.formats, R.layout.spinner_item)
        formatSpin.adapter = formatAdapter
        formatSpin.setSelection(arguments!!.getInt("format"))

        val isoSpin = rootView.findViewById<View>(R.id.iso_spinner) as Spinner
        val isoAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.isos, R.layout.spinner_item)
        isoSpin.adapter = isoAdapter
        isoSpin.setSelection(arguments!!.getInt("iso"))

        val framesText = rootView.findViewById<View>(R.id.frames_edit) as EditText
        framesText.setSelection(framesText.text.length)
        framesText.setText(arguments!!.getInt("frames").toString(), TextView.BufferType.EDITABLE)

        val devSpin = rootView.findViewById<View>(R.id.dev_spinner) as Spinner
        val devAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.devs, R.layout.spinner_item)
        devSpin.adapter = devAdapter
        devSpin.setSelection(arguments!!.getInt("dev"))

        val notesText = rootView.findViewById<View>(R.id.notes_edit) as TextView
        notesText.text = arguments!!.getString("notes")

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            // This gets called if the user wants to truncate the film list
            fun saveData() {
                (activity as MainActivity).setFilmData(
                        manuText.text.toString(),
                        filmText.text.toString(),
                        formatSpin.selectedItemPosition,
                        isoSpin.selectedItemPosition,
                        Integer.parseInt(framesText.text.toString()),
                        devSpin.selectedItemPosition,
                        notesText.text.toString() )
                dismiss()
            }

            // Warn if new frames size is smaller than old
            // Ask user if they'd like to proceed or whatever
            if (Integer.parseInt(framesText.text.toString()) < arguments!!.getInt("frames")) {
                val alertBuilder = AlertDialog.Builder(rootView.context)
                alertBuilder.setMessage("New frame count is less than current.\nThe frame list will be truncated!\nProceed?")
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


    companion object {
        private val LOG_TAG = FilmDialog::class.java.simpleName
    }
}
