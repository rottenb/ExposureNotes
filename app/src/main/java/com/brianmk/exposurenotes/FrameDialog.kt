package com.brianmk.exposurenotes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class FrameDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.frame_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val position = arguments!!.getInt("position")

        val shutterSpin = rootView.findViewById<View>(R.id.shutter_spinner) as Spinner
        val shutterAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.shutter_speeds, R.layout.spinner_item)
        shutterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shutterSpin.adapter = shutterAdapter
        shutterSpin.setSelection(arguments!!.getInt("shutter"))

        val apertureSpin = rootView.findViewById<View>(R.id.aperture_spinner) as Spinner
        val apertureAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.apertures, R.layout.spinner_item)
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apertureSpin.adapter = apertureAdapter
        apertureSpin.setSelection(arguments!!.getInt("aperture"))

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lensAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.lenses, R.layout.spinner_item)
        lensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lensSpin.adapter = lensAdapter
        lensSpin.setSelection(arguments!!.getInt("lens"))

        val notesText = rootView.findViewById<View>(R.id.frame_notes_edit) as TextView
        notesText.text = arguments!!.getString("notes")

        val okButton = rootView.findViewById<View>(R.id.frame_ok_button) as Button
        okButton.setOnClickListener {
            (activity as MainActivity).setSingleFrameData(position,
                    shutterSpin.selectedItemPosition,
                    apertureSpin.selectedItemPosition,
                    lensSpin.selectedItemPosition,
                    notesText.text.toString())
            dismiss()
        }

        val clearButton = rootView.findViewById<View>(R.id.frame_clear_button) as Button
        clearButton.setOnClickListener {
            shutterSpin.setSelection(0)
            apertureSpin.setSelection(0)
            lensSpin.setSelection(0)
            notesText.text = ""
        }

        return rootView
    }

    companion object {
        private val LOG_TAG = FrameDialog::class.java.simpleName
    }
}
