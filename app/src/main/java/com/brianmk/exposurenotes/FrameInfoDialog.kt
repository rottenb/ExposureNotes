package com.brianmk.exposurenotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class FrameInfoDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.frame_dialog, container)

        val position = arguments!!.getInt("position")

        val shutterSpin = rootView.findViewById<Spinner>(R.id.set_shutter)
        val shutterAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.shutter_speeds, R.layout.spinner_item)
        shutterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shutterSpin.adapter = shutterAdapter
        shutterSpin.setSelection(arguments!!.getInt("shutter"))

        val apertureSpin = rootView.findViewById<Spinner>(R.id.set_aperture)
        val apertureAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.apertures, R.layout.spinner_item)
        apertureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apertureSpin.adapter = apertureAdapter
        apertureSpin.setSelection(arguments!!.getInt("aperture"))

        val notesText = rootView.findViewById<TextView>(R.id.detail_notes)
        notesText.text = arguments!!.getString("notes")

        val okButton = rootView.findViewById<Button>(R.id.frame_ok_button)
        okButton.setOnClickListener {
            (activity as MainActivity).setSingleFrameData(position,
                    shutterSpin.selectedItemPosition,
                    apertureSpin.selectedItemPosition,
                    notesText.text.toString())

            dismiss()
        }

        return rootView
    }


    /*
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater
        val content = inflater.inflate(R.layout.frame_dialog, null)

        builder.setView(content)

        // Shutter spinner
        Log.d(LOG_TAG, "Here")

        val shutSpin = content.findViewById<Spinner>(R.id.set_shutter)
        val shutAdapter = ArrayAdapter.createFromResource(context,
                R.array.shutter_speeds, R.layout.shutter_spinner_item)
        Log.d(LOG_TAG, "THERE")
        shutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        shutSpin.adapter = shutAdapter
        shutSpin.setSelection(arguments.getInt("shutter"))

        // Aperture spinner
        val aptSpin = content.findViewById<Spinner>(R.id.set_aperture)
        val aptAdapter = ArrayAdapter.createFromResource(context,
                R.array.apertures, R.layout.shutter_spinner_item)
        aptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        aptSpin.adapter = aptAdapter
        aptSpin.setSelection(arguments.getInt("aperture"))

        // Notes
        val notesEdit = content.findViewById<EditText>(R.id.detail_notes)
        notesEdit.setText(arguments.getString("notes"))

        // Save info and return to main screen
        val okButton = content.findViewById<Button>(R.id.frame_ok_button)
        okButton.setOnClickListener {
            val pos = arguments.getInt("position")
            val t = shutSpin.selectedItemPosition
            val a = aptSpin.selectedItemPosition
            val n = notesEdit.text.toString()

            (activity as MainActivity).setSingleFrameData(pos, t, a, n)

            dismiss()
        }

        return builder.create()
    }
*/
    companion object {
        private val LOG_TAG = FrameInfoDialog::class.java.simpleName
    }
}
