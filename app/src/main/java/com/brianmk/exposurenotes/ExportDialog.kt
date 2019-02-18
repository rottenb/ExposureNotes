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

class ExportDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.export_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val filenameText = rootView.findViewById<View>(R.id.filename_edit) as TextView
        val filenameStr = "01 - ${arguments!!.getString("camera")} - ${arguments!!.getString("film")}.json"
        filenameText.text = filenameStr

        val methodSpin = rootView.findViewById<View>(R.id.method_spinner) as Spinner
        val methodAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.export_methods, R.layout.spinner_item)
        methodSpin.adapter = methodAdapter
        methodSpin.setSelection(0)

        val okButton = rootView.findViewById<View>(R.id.ok_button) as Button
        okButton.setOnClickListener {
            (activity as MainActivity).exportFilmRoll(filenameText.text.toString(),
                    methodSpin.selectedItem.toString())

            dismiss()
        }

        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button
        cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }
    companion object {
        private val LOG_TAG = ExportDialog::class.java.simpleName
    }
}
