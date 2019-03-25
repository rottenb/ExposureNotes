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

class LensDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_lens, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val addNewStr = rootView.resources.getString(R.string.add_new)

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lenses = mutableListOf(addNewStr)
        lenses.addAll(arguments?.getStringArray("lenses")!!)
        lensSpin.adapter = ArrayAdapter(rootView.context, R.layout.item_spinner, lenses)
        lensSpin.setSelection(arguments!!.getInt("lensIdx"))

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

        val modelText = rootView.findViewById<View>(R.id.model_edit) as TextView

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            if (makerText.text.toString() == "" || modelText.text.toString() == "") {
                Toast.makeText(rootView.context, "Make and Model required!", Toast.LENGTH_LONG).show()
            } else {
                (activity as MainActivity).setLensData(
                        makerText.text.toString(),
                        modelText.text.toString(),
                        save = true)
                dismiss()
            }
        }

        val clearButton = rootView.findViewById<View>(R.id.clear_button) as Button
        clearButton.setOnClickListener {
            if (clearButton.text == getString(R.string.remove_button)) {
                val alertBuilder = AlertDialog.Builder(rootView.context)
                alertBuilder.setMessage("Note: Any frame that used this lens will be reset.")
                alertBuilder.setPositiveButton("Ok") { _, _ ->
                    makerList.remove(addNewStr)
                    (activity as MainActivity).setLensData(
                            makerText.text.toString(),
                            modelText.text.toString(),
                            delete = true,
                            lensIdx = lensSpin.selectedItemPosition - 1,
                            save = true)

                    dismiss()
                }
                alertBuilder.setNegativeButton("No, wait") { _, _ -> } // do nothing

                val warnDialog: Dialog = alertBuilder.create()
                warnDialog.show()

            } else {
                makerText.setText("")
                modelText.text = ""
            }
        }

        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button
        cancelButton.setOnClickListener {
            dismiss()
        }

        lensSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (pos == 0) {
                    makerText.setText("")
                    modelText.text = ""
                    saveButton.setText(R.string.add_button)
                    clearButton.setText(R.string.clear_button)
                } else {
                    saveButton.setText(R.string.save_button)
                    clearButton.setText(R.string.remove_button)
                    for (i in 0 until makerList.size) {
                        if (lensSpin.selectedItem.toString().startsWith(makerList[i])) {
                            makerText.setText(makerList[i])
                            makerText.dismissDropDown()
                            modelText.text = lensSpin.selectedItem.toString().substringAfter("${makerList[i]} ")
                        }
                    }
                }
            }

        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = CameraDialog::class.java.simpleName
    }
}