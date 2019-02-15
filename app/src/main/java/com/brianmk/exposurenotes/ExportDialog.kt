package com.brianmk.exposurenotes

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import java.util.*

class ExportDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        val content = activity.layoutInflater.inflate(R.layout.export_dialog, null)
        builder.setView(content)


        val cal = Calendar.getInstance()
//        val filename = Integer.toString(cal.get(cal.DAY_OF_MONTH)) + "-" +
//                Integer.toString(cal.get(cal.MONTH)) + "-" +
//                Integer.toString(cal.get(cal.YEAR)) + " " +
//                arguments.getString("film") + ".json"

        val filename = "test.json"

        // Default filename
        val filenameText = content.findViewById<EditText>(R.id.export_filename)
        filenameText.setText(filename)

        // Export method
        val methodSpinner = content.findViewById<Spinner>(R.id.default_spinner)
        val activityAdapter = ArrayAdapter.createFromResource(content.context,
                R.array.export_methods, R.layout.default_spinner_item)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        methodSpinner.adapter = activityAdapter
        methodSpinner.setSelection(0)


        // Export button
        val exportButton = content.findViewById<Button>(R.id.export_dialog_button)
        exportButton.setOnClickListener( {
            fun onClick(view: View) {

                (activity as MainActivity).exportFilmRoll("$filename.json", "dropbox")

                dismiss()
            }
        })

        // Cancel button
        val cancelButton = content.findViewById<Button>(R.id.cancel_dialog_button)
        cancelButton.setOnClickListener( {
            fun onClick(view: View) {
                dismiss()
            }
        })

        return builder.create()
    }

    companion object {
        private val LOG_TAG = ExportDialog::class.java.simpleName
    }
}
