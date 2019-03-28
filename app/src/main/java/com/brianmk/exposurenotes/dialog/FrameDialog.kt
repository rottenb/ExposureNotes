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

        val shutterSpin = rootView.findViewById<View>(R.id.shutter_spinner) as Spinner
        val shutters = rootView.resources.getStringArray(R.array.shutter_speeds)
        shutters[0] = "0s"
        shutterSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, shutters)
        shutterSpin.setSelection(arguments?.getInt("shutter")!!)

        val apertureSpin = rootView.findViewById<View>(R.id.aperture_spinner) as Spinner
        val apertures = rootView.resources.getStringArray(R.array.apertures)
        apertures[0] = "f/0.0"
        apertureSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, apertures)
        apertureSpin.setSelection(arguments?.getInt("aperture")!!)

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lenses = arguments?.getStringArray("lenses")!!
        lensSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, lenses)
        for (i in 0 until lenses.size) {
            if (lenses[i] == arguments?.getString("lens")) {
                lensSpin.setSelection(i)
            }

        }
        //lensSpin.setSelection(arguments?.getInt("lensIdx")!!)

        val notesText = rootView.findViewById<View>(R.id.frame_notes) as TextView
        notesText.text = arguments?.getString("notes")

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            (activity as MainActivity).setFrameData(arguments?.getInt("position")!!,
                    shutterSpin.selectedItemPosition,
                    apertureSpin.selectedItemPosition,
                    lensSpin.selectedItem.toString(),
                    notesText.text.toString(),
                    true)

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
