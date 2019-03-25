package com.brianmk.exposurenotes.dialog

import android.app.Dialog
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

class FilmDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_film, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
/*
        val makerText = rootView.findViewById<View>(R.id.maker_edit) as AutoCompleteTextView
        makerText.setText(arguments?.getString("maker"))
        val makerAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, arguments?.getStringArray("makers")!!)
        makerText.setAdapter(makerAdapter)
*/
        val makerList: MutableList<String> = mutableListOf()
        makerList.addAll(arguments?.getStringArray("makers")!!)
        val makerText = rootView.findViewById<View>(R.id.maker_edit) as AutoCompleteTextView
        makerText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, makerList))
        makerText.setText(arguments?.getString("maker"))
        // Holding on the item deletes it from the global list, at the user's option
        makerText.setOnLongClickListener {
            val alertBuilder = android.app.AlertDialog.Builder(rootView.context)
            alertBuilder.setMessage("Remove ${makerText.text} from autocomplete list?")
            // Remove item
            alertBuilder.setPositiveButton("Yes") { _, _ ->
                makerList.remove(makerText.text.toString())
                makerText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, makerList))
                makerText.setText("")
                (activity as MainActivity).setProductNames(makers = makerList)
            }
            alertBuilder.setNegativeButton("No") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()

            true
        }
/*
        val filmText = rootView.findViewById<View>(R.id.film_edit) as AutoCompleteTextView
        filmText.setText(arguments?.getString("model"))
        val filmAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, arguments?.getStringArray("models")!!)
        filmText.setAdapter(filmAdapter)
*/
        val filmList: MutableList<String> = mutableListOf()
        filmList.addAll(arguments?.getStringArray("models")!!)
        val filmText = rootView.findViewById<View>(R.id.film_edit) as AutoCompleteTextView
        filmText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, filmList))
        filmText.setText(arguments?.getString("model"))
        // Holding on the item deletes it from the global list, at the user's option
        filmText.setOnLongClickListener {
            val alertBuilder = android.app.AlertDialog.Builder(rootView.context)
            alertBuilder.setMessage("Remove ${filmText.text} from autocomplete list?")
            // Remove item
            alertBuilder.setPositiveButton("Yes") { _, _ ->
                filmList.remove(makerText.text.toString())
                filmText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, filmList))
                filmText.setText("")
                (activity as MainActivity).setProductNames(filmModels = filmList)

            }
            alertBuilder.setNegativeButton("No") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()

            true
        }

        val framePicker = rootView.findViewById<View>(R.id.frame_count) as NumberPicker
        framePicker.minValue = 0
        framePicker.maxValue = 40

        var frameCount = arguments!!.getInt("frames")

        val numberText = rootView.findViewById<View>(R.id.number_text) as TextView
        numberText.text = frameCount.toString()

        numberText.setOnClickListener {
            framePicker.visibility = View.VISIBLE
        }

        framePicker.setOnClickListener {
            frameCount = framePicker.value
            numberText.text = frameCount.toString()
            framePicker.visibility = View.GONE
        }

        val isoSpin = rootView.findViewById<View>(R.id.iso_spinner) as Spinner
        val isoAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.isos, R.layout.item_spinner)
        isoSpin.adapter = isoAdapter
        isoSpin.setSelection(arguments!!.getInt("iso"))

        val devSpin = rootView.findViewById<View>(R.id.dev_spinner) as Spinner
        val devAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.devs, R.layout.item_spinner)
        devSpin.adapter = devAdapter
        devSpin.setSelection(arguments!!.getInt("dev"))

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            fun saveData() {
                if( makerText.text.toString() == "" || filmText.text.toString() == "" || frameCount == 0) {
                    Toast.makeText(rootView.context, "Make, model, and frame count is required!", Toast.LENGTH_LONG).show()
                }
                else {
                    (activity as MainActivity).setFilmData(
                            makerText.text.toString(),
                            filmText.text.toString(),
                            isoSpin.selectedItemPosition,
                            frameCount,
                            devSpin.selectedItemPosition,
                            true)
                    dismiss()
                }
            }

            // Warn if new frames size is smaller than old
            // Ask user if they'd like to proceed or whatever
            if (frameCount < arguments!!.getInt("frames")) {
                val alertBuilder = AlertDialog.Builder(rootView.context)
                alertBuilder.setMessage("New frame count is less than the current frame count.\n\n" +
                        "The frame list will be truncated and data will be lost!\n\nProceed?")
                alertBuilder.setPositiveButton("YES") { _, _ -> saveData() }
                alertBuilder.setNegativeButton("NO") { _, _ -> } // do nothing

                val warnDialog: Dialog = alertBuilder.create()
                warnDialog.show()
            } else {
                saveData()
            }
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
        private val LOG_TAG = FilmDialog::class.java.simpleName
    }
}
