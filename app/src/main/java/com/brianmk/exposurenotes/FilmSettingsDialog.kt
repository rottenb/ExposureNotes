package com.brianmk.exposurenotes

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class FilmSettingsDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        val content = activity.layoutInflater.inflate(R.layout.film_settings_dialog, null)
        builder.setView(content)

        // ISO
        val isoSpinner = content.findViewById<Spinner>(R.id.default_spinner)
        val activityAdapter = ArrayAdapter.createFromResource(content.context,
                R.array.iso_types, R.layout.default_spinner_item)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        isoSpinner.adapter = activityAdapter
        isoSpinner.setSelection(2)

        // Dev notes
        val devSpinner = content.findViewById<Spinner>(R.id.another_spinner)
        val actAdapter = ArrayAdapter.createFromResource(content.context,
                R.array.dev_types, R.layout.default_spinner_item)
        actAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        devSpinner.adapter = actAdapter
        devSpinner.setSelection(2)

        // Save button
        val saveButton = content.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            dismiss()
        }

        // Cancel button
        val cancelButton = content.findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

    companion object {
        private val LOG_TAG = FilmSettingsDialog::class.java.simpleName
    }
}
