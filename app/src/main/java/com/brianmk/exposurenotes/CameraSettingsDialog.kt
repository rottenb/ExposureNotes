package com.brianmk.exposurenotes

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class CameraSettingsDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        val content = activity.layoutInflater.inflate(R.layout.camera_settings_dialog, null)
        builder.setView(content)

        // Film formats
        val formatSpinner = content.findViewById<Spinner>(R.id.default_spinner)
        val activityAdapter = ArrayAdapter.createFromResource(content.context,
                R.array.format_types, R.layout.default_spinner_item)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        formatSpinner.adapter = activityAdapter
        formatSpinner.setSelection(0)

        // Save button
        val saveButton = content.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener( {
            fun onClick(v: View) {
                // save data stuff goes here
                dismiss()
            }
        })

        // Cancel button
        val cancelButton = content.findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener( {
            fun onClick(v: View) {
                dismiss()
            }
        })

        return builder.create()
    }

    companion object {
        private val LOG_TAG = CameraSettingsDialog::class.java.simpleName
    }
}
