package com.brianmk.exposurenotes.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.brianmk.exposurenotes.MainActivity
import com.brianmk.exposurenotes.R


class CameraDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_camera, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        // Camera manufacturer edit
        val makerList: MutableList<String> = mutableListOf()
        makerList.addAll(arguments?.getStringArray("makers")!!)
        val makerText = rootView.findViewById<View>(R.id.maker_edit) as AutoCompleteTextView
        makerText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, makerList))
        makerText.setText(arguments?.getString("maker"))
        // Holding on the item deletes it from the global list, at the user's option
        makerText.setOnLongClickListener {
            val alertBuilder = AlertDialog.Builder(rootView.context)
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

        // Camera model name edit
        val modelList: MutableList<String> = mutableListOf()
        modelList.addAll(arguments?.getStringArray("models")!!)
        val modelText = rootView.findViewById<View>(R.id.camera_edit) as AutoCompleteTextView
        modelText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, modelList))
        modelText.setText(arguments?.getString("model"))
        // Holding on the item deletes it from the global list, at the user's option
        modelText.setOnLongClickListener {
            val alertBuilder = AlertDialog.Builder(rootView.context)
            alertBuilder.setMessage("Remove ${modelText.text} from autocomplete list?")
            // Remove item
            alertBuilder.setPositiveButton("Yes") { _, _ ->
                modelList.remove(modelText.text.toString())
                modelText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, modelList))
                modelText.setText("")
                (activity as MainActivity).setProductNames(cameraModels = modelList)
            }
            alertBuilder.setNegativeButton("No") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()

            true
        }
        // Camera formats (35, 120, 5x7, 8x10, etc)
        val formatSpin = rootView.findViewById<View>(R.id.format_spinner) as Spinner
        formatSpin.adapter = ArrayAdapter.createFromResource(rootView.context, R.array.camera_formats, R.layout.item_spinner)
        formatSpin.setSelection(arguments?.getInt("formatIdx")!!)

        // Lens attached to this camera (+ edit list on long press?)
        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lenses = arguments?.getStringArray("lenses")!!
        lensSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, lenses)
        for (i in 0 until lenses.size) {
            if (lenses[i] == arguments?.getString("lens")) {
                lensSpin.setSelection(i)
            }
        }
        //lensSpin.setSelection(arguments?.getInt("lensIdx")!!)

        // Don't let the user save unless make/model is set
        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            if (makerText.text.toString() == "" || modelText.text.toString() == "") {
                Toast.makeText(rootView.context, "Make and Model required!", Toast.LENGTH_LONG).show()
            } else {
                if (!makerList.contains(makerText.text.toString())) {
                    makerList.add(makerText.text.toString())
                }
                if (!modelList.contains(modelText.text.toString())) {
                    modelList.add(modelText.text.toString())
                }
                (activity as MainActivity).setCameraData(
                        makerText.text.toString(),
                        modelText.text.toString(),
                        formatSpin.selectedItemPosition,
                        lensSpin.selectedItem.toString(),
                        true)

                dismiss()
            }
        }

        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button
        cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    // This ensures the dialog fills the screen
    override fun onResume() {
        super.onResume()

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
     }


    companion object {
        private val LOG_TAG = CameraDialog::class.java.simpleName
    }
}
