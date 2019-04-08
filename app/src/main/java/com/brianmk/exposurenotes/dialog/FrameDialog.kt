package com.brianmk.exposurenotes.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.brianmk.exposurenotes.MainActivity
import com.brianmk.exposurenotes.R

class FrameDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_frame, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
        // Help information for this dialog
        rootView.findViewById<View>(R.id.info_text).setOnClickListener { showHelp(rootView.context) }

        /* VIEWS */
        val shutterSpin = rootView.findViewById<View>(R.id.shutter_spinner) as Spinner
        val apertureSpin = rootView.findViewById<View>(R.id.aperture_spinner) as Spinner
        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val focalText = rootView.findViewById<View>(R.id.focal_length) as EditText
        val notesText = rootView.findViewById<View>(R.id.frame_notes) as TextView
        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        val clearButton = rootView.findViewById<View>(R.id.clear_button) as Button
        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button

        /* DATA */
        val shutters = rootView.resources.getStringArray(R.array.shutter_speeds)
        shutters[0] = "0s"
        shutterSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, shutters)
        shutterSpin.setSelection(arguments?.getInt("shutter")!!)

        val apertures = rootView.resources.getStringArray(R.array.apertures)
        apertures[0] = "f/0.0"
        apertureSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, apertures)
        apertureSpin.setSelection(arguments?.getInt("aperture")!!)

        val lensList = arguments?.getStringArray("lenses")!!
        lensSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, lensList)
        for (i in 0 until lensList.size) {
            if (lensList[i] == arguments?.getString("lens")) {
                lensSpin.setSelection(i)
            }
        }

        focalText.setText(arguments?.getInt("focal_length").toString())

        notesText.text = arguments?.getString("notes")

        /* LISTENERS */
        saveButton.setOnClickListener {
            (activity as MainActivity).setFrameData(arguments?.getInt("position")!!,
                    shutterSpin.selectedItemPosition,
                    apertureSpin.selectedItemPosition,
                    Integer.parseInt(focalText.text.toString()),
                    lensSpin.selectedItem.toString(),
                    notesText.text.toString(),
                    true)

            dismiss()
        }

        clearButton.setOnClickListener {
            shutterSpin.setSelection(0)
            apertureSpin.setSelection(0)
            lensSpin.setSelection(0)
            notesText.text = ""
        }

        cancelButton.setOnClickListener {
            (activity as MainActivity).cancelFrame(arguments?.getBoolean("new_frame")!!)
            dismiss()
        }

        return rootView
    }

    // Help information for this dialog
    private fun showHelp(context: Context) {
        val infoBuilder = AlertDialog.Builder(context)
        infoBuilder.setTitle("Frame Settings:")
        infoBuilder.setMessage(resources.getString(R.string.frame_dialog_info))
        infoBuilder.setPositiveButton("Ok") { _, _ -> } // Do nothing, just disappear
        infoBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    companion object {
        private val LOG_TAG = FrameDialog::class.java.simpleName
    }
}
