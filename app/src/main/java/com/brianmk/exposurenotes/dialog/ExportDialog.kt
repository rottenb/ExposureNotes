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

class ExportDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_export, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val filenameText = rootView.findViewById<View>(R.id.filename_edit) as TextView
        val filenameStr = "01 - ${arguments!!.getString("camera")} - ${arguments!!.getString("film")}.json"
        filenameText.text = filenameStr

        val saveSpin = rootView.findViewById<View>(R.id.save_spinner) as Spinner
        val saveAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.export_methods, R.layout.item_spinner)
        saveSpin.adapter = saveAdapter
        saveSpin.setSelection(0)

        val okButton = rootView.findViewById<View>(R.id.ok_button) as Button
        okButton.setOnClickListener {
            (activity as MainActivity).exportFilmRoll(filenameText.text.toString(),
                    saveSpin.selectedItem.toString())

            dismiss()
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
        private val LOG_TAG = ExportDialog::class.java.simpleName
    }
}
