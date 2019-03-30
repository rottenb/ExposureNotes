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
import com.brianmk.exposurenotes.adapter.LensArrayAdapter

class LensDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(com.brianmk.exposurenotes.R.layout.dialog_lens, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        // Help information for this dialog
        rootView.findViewById<View>(R.id.info_text).setOnClickListener {
            val infoBuilder = AlertDialog.Builder(rootView.context)
            infoBuilder.setTitle("Instructions for Lens Selection:")
            infoBuilder.setMessage(resources.getString(R.string.lens_dialog_info))
            infoBuilder.setPositiveButton("Ok") { _, _ -> } // Do nothing, just disappear
            infoBuilder.create().show()
        }

        // Editable Text
        val lensListView = rootView.findViewById<View>(R.id.lens_list) as ListView
        val makerText = rootView.findViewById<View>(R.id.maker_edit) as AutoCompleteTextView
        val modelText = rootView.findViewById<View>(R.id.model_edit) as EditText

        // Buttons
        val addButton = rootView.findViewById<View>(R.id.add_button) as Button
        val clearButton = rootView.findViewById<View>(R.id.clear_button) as Button
        val closeButton = rootView.findViewById<View>(R.id.close_button) as Button

        // Data and such
        val lensList: MutableList<String> = mutableListOf()
        lensList.addAll(arguments?.getStringArray("lenses")!!)
        var currentLens = -1
        val lensAdapter = LensArrayAdapter(rootView.context, R.layout.item_lens_list, lensList)
        lensListView.adapter = lensAdapter
        val makerList = arguments?.getStringArray("makers")!!
        makerText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, makerList))

        // Event Listeners
        makerText.setOnClickListener {
            if (makerText.text.toString() == "") {
                addButton.text = resources.getString(R.string.add_button)
            }
        }

        modelText.setOnClickListener {
            if (modelText.text.toString() == "") {
                addButton.text = resources.getString(R.string.add_button)
            }
        }

        // Populates the text edit fields with existing lens info
        lensListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            currentLens = pos
            addButton.text = resources.getString(R.string.update_button)
            for (i in 0 until makerList.size ) {
                if (lensList[pos].contains(makerList[i])) {
                    makerText.setText(makerList[i])
                    //modelText.setText(lensList[pos].split(delim)[1])
                    modelText.setText(lensList[pos].split("${makerList[i]} ")[1])
                    break
                }
            }
        }

        // Remove the lens from the list
        lensListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, pos, _ ->
            val alertBuilder = AlertDialog.Builder(rootView.context)
            alertBuilder.setMessage("Note: Any frame that used this lens will be reset.")
            alertBuilder.setPositiveButton("Ok") { _, _ ->
                (activity as MainActivity).updateLensData(makerText.text.toString(), lensList[pos], "Unknown")
                (activity as MainActivity).setLensData(makerText.text.toString(), lensList, save = true)

                lensList.removeAt(pos)
                lensList.sort()
                lensAdapter.notifyDataSetChanged()

                currentLens = -1

                makerText.setText("")
                modelText.setText("")

            }
            alertBuilder.setNegativeButton("No, wait") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()

            true
        }

        // If new lens, add to list
        // Update existing lens
        addButton.setOnClickListener {
            // If either make or model fields are empty, warn the user and then don't do anything
            if (makerText.text.toString() == "" || modelText.text.toString() == "") {
                Toast.makeText(rootView.context, "Make and Model required!", Toast.LENGTH_LONG).show()
            } else {
                // Add new lens to list
                if (addButton.text.toString() == resources.getString(R.string.add_button)) {
                    val modelStr = "${makerText.text} ${modelText.text}"
                    if (!lensList.contains(modelStr)) {
                        lensList.add(modelStr)

                        (activity as MainActivity).setLensData(makerText.text.toString(), lensList, save = true)
                    }
                } else if (addButton.text.toString() == resources.getString(R.string.update_button)) {
                    // Update current list item
                    val lens = "${makerText.text} ${modelText.text}"
                    (activity as MainActivity).updateLensData(makerText.text.toString(), lensList[currentLens], lens)
                    (activity as MainActivity).setLensData(makerText.text.toString(), lensList, save = true)

                    lensList[currentLens] = lens
                    currentLens = -1
                }

                lensList.sort()
                lensAdapter.notifyDataSetChanged()

                makerText.setText("")
                modelText.setText("")
            }
        }


        clearButton.setOnClickListener {
            makerText.setText("")
            modelText.setText("")
        }

        closeButton.setOnClickListener {
            dismiss()
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





/*
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
                    //makerList.remove(addNewStr)
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
*/