package com.brianmk.exposurenotes.dialog

import android.app.Dialog
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
import java.io.File

class ExportDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_export, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
        // Help information for this dialog
        rootView.findViewById<View>(R.id.info_text).setOnClickListener { showHelp(rootView.context) }

        /* VIEWS */
        val exportAsSpin = rootView.findViewById<View>(R.id.export_as_spinner) as Spinner
        val exportToSpin = rootView.findViewById<View>(R.id.export_to_spinner) as Spinner
        val filenameText = rootView.findViewById<View>(R.id.filename_edit) as TextView
        val exportButton = rootView.findViewById<View>(R.id.export_button) as Button
        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button

        /* DATA */
        val filename = "01 - ${arguments!!.getString("camera")} - ${arguments!!.getString("film")}"
        var extension = ".json"
        var fullFilename = filename + extension
        filenameText.text = fullFilename

        val exportAsAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_spinner, resources.getStringArray(R.array.export_types))
        exportAsSpin.adapter = exportAsAdapter
        val exportToAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_spinner, resources.getStringArray(R.array.export_methods))
        exportToSpin.adapter = exportToAdapter

        /* LISTENERS */
        exportAsSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                extension = resources.getStringArray(R.array.export_file_ext)[pos]
                fullFilename = filename + extension
                filenameText.text = fullFilename
            }
        }

        exportButton.setOnClickListener {
            // Check if file exists and warn the user of over-writes
            val filepath = File("storage/emulated/0/ExposureNotes/")
            filepath.mkdirs()

            if (File("$filepath/${filenameText.text}").exists()) {
                val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(rootView.context)
                alertBuilder.setTitle(resources.getString(R.string.file_exists_title))
                alertBuilder.setMessage(resources.getString(R.string.file_exists_msg))

                // Over-write
                alertBuilder.setPositiveButton("Over-write") { _, _ ->
                    (activity as MainActivity).exportFilmRoll(filenameText.text.toString(), exportToSpin.selectedItemPosition, exportAsSpin.selectedItemPosition)
                    dismiss()
                }

                // Cancel Everything
                alertBuilder.setNegativeButton("Cancel") { _, _ -> } // Do Nothing

                val warnDialog: Dialog = alertBuilder.create()
                warnDialog.show()
            } else {
                (activity as MainActivity).exportFilmRoll(filenameText.text.toString(), exportToSpin.selectedItemPosition, exportAsSpin.selectedItemPosition)
                dismiss()
            }

        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    // Help information for this dialog
    private fun showHelp(context: Context) {
        val infoBuilder = AlertDialog.Builder(context)
        infoBuilder.setTitle("Export Settings:")
        infoBuilder.setMessage(resources.getString(R.string.export_dialog_info))
        infoBuilder.setPositiveButton("Ok") { _, _ -> } // Do nothing, just disappear
        infoBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = ExportDialog::class.java.simpleName
    }
}
