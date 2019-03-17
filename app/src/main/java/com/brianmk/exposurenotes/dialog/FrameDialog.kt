package com.brianmk.exposurenotes.dialog

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
import com.brianmk.exposurenotes.MainActivity
import com.brianmk.exposurenotes.R

class FrameDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_frame, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val position = arguments!!.getInt("position")

        val shutterSpin = rootView.findViewById<View>(R.id.shutter_spinner) as Spinner
        val shutterAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.shutter_speeds, R.layout.item_spinner)
        shutterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shutterSpin.adapter = shutterAdapter
        shutterSpin.setSelection(arguments!!.getInt("shutter"))

        val apertureSpin = rootView.findViewById<View>(R.id.aperture_spinner) as Spinner
        val apertureAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.apertures, R.layout.item_spinner)
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apertureSpin.adapter = apertureAdapter
        apertureSpin.setSelection(arguments!!.getInt("aperture"))

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lensAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.lenses, R.layout.item_spinner)
        lensAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lensSpin.adapter = lensAdapter
        lensSpin.setSelection(arguments!!.getInt("lens"))
        val isFixed = arguments!!.getBoolean("fixed")
        if (isFixed) {
            lensSpin.visibility = View.INVISIBLE
        }

        val lensSpinMask = rootView.findViewById<View>(R.id.lens_spinner_mask) as TextView
        lensSpinMask.text = lensSpin.selectedItem.toString()
        if (isFixed) {
            lensSpinMask.visibility = View.VISIBLE
            (rootView.findViewById<View>(R.id.lens_header) as TextView).text = getString(R.string.lens_fixed_select)
        }

        val notesText = rootView.findViewById<View>(R.id.frame_notes_edit) as TextView
        notesText.text = arguments!!.getString("notes")

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            (activity as MainActivity).setSingleFrameData(position,
                    shutterSpin.selectedItemPosition,
                    apertureSpin.selectedItemPosition,
                    lensSpin.selectedItemPosition,
                    notesText.text.toString())
            dismiss()
        }

        val clearButton = rootView.findViewById<View>(R.id.clear_button) as Button
        clearButton.setOnClickListener {
            shutterSpin.setSelection(0)
            apertureSpin.setSelection(0)
            lensSpin.setSelection(0)
            notesText.text = ""
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
        private val LOG_TAG = FrameDialog::class.java.simpleName
    }
}
