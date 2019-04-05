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

class LensDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(com.brianmk.exposurenotes.R.layout.dialog_lens, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
        // Help information for this dialog
        rootView.findViewById<View>(R.id.info_text).setOnClickListener { showHelp(rootView.context) }

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
        val lensAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, lensList)
        lensListView.adapter = lensAdapter
        val makerList = arguments?.getStringArray("makers")!!
        makerText.setAdapter(ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, makerList))

        /* Event Listeners */
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
                    modelText.setText(lensList[pos].split("${makerList[i]} ")[1])
                    break
                }
            }
        }

        // Remove the lens from the list
        lensListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, pos, _ ->
            val alertBuilder = AlertDialog.Builder(rootView.context)
            alertBuilder.setTitle(resources.getString(R.string.delete_alert_title))
            alertBuilder.setMessage(resources.getString(R.string.lens_delete_msg))
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
                addButton.text = resources.getString(R.string.add_button)

            }
        }


        clearButton.setOnClickListener {
            makerText.setText("")
            modelText.setText("")
            currentLens = -1
            addButton.text = resources.getString(R.string.add_button)
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun showHelp(context: Context) {
        // Help information for this dialog
        val infoBuilder = AlertDialog.Builder(context)
        infoBuilder.setTitle("Lens Settings:")
        infoBuilder.setMessage(resources.getString(R.string.lens_dialog_info))
        infoBuilder.setPositiveButton("Ok") { _, _ -> } // Do nothing, just disappear
        infoBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = LensDialog::class.java.simpleName
    }
}