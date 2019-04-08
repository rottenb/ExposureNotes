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

class AutocompleteDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(com.brianmk.exposurenotes.R.layout.dialog_autocomplete, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
        // Help information for this dialog
        rootView.findViewById<View>(R.id.info_text).setOnClickListener { showHelp(rootView.context) }

        /* VIEWS */
        val groupSpin = rootView.findViewById<View>(R.id.group_spinner) as Spinner
        val itemListView = rootView.findViewById<View>(R.id.item_list) as ListView
        val itemText = rootView.findViewById<View>(R.id.item_edit) as EditText
        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        val closeButton = rootView.findViewById<View>(R.id.close_button) as Button

        /* DATA */
        var currentList: Int = -1
        var currentItem: Int = -1

        var makerList: MutableList<String> = mutableListOf()
        makerList.addAll(arguments?.getStringArray("makers")!!)
        makerList.sort()

        var cameraList: MutableList<String> = mutableListOf()
        cameraList.addAll(arguments?.getStringArray("cameras")!!)
        cameraList.sort()

        var filmList: MutableList<String> = mutableListOf()
        filmList.addAll(arguments?.getStringArray("film")!!)
        filmList.sort()

        var lensList: MutableList<String> = mutableListOf()
        lensList.addAll(arguments?.getStringArray("lenses")!!)
        lensList.sort()

        val groupAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_spinner, resources.getStringArray(R.array.autocomplete_groups) )
        groupSpin.adapter = groupAdapter

        val itemList: MutableList<String> = mutableListOf()
        itemList.addAll(makerList)
        val itemListAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_spinner, itemList)
        itemListView.adapter = itemListAdapter

        /* LISTENERS */
        // Bring up specific group of items
        groupSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) { }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                itemList.clear()
                when (pos) {
                    0 -> itemList.addAll(makerList)
                    1 -> itemList.addAll(cameraList)
                    2 -> itemList.addAll(filmList)
                    3 -> itemList.addAll(lensList)
                }

                itemListAdapter.notifyDataSetChanged()

                currentList = pos
            }
        }

        // Tap on list entry to edit
        itemListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            itemText.setText(itemListView.getItemAtPosition(pos).toString())
            currentItem = pos

            saveButton.text = resources.getString(R.string.update_button)
        }

        // Hold on list entry to prompt delete
        itemListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, pos, _ ->
            val alertBuilder = AlertDialog.Builder(rootView.context)
            alertBuilder.setTitle(resources.getString(R.string.delete_alert_title))
            alertBuilder.setMessage(resources.getString(R.string.item_delete_msg))
            alertBuilder.setPositiveButton("Ok") { _, _ ->
                itemList.removeAt(pos)
                itemList.sort()
                itemListAdapter.notifyDataSetChanged()
                when (currentList) {
                    0 -> makerList = itemList
                    1 -> cameraList = itemList
                    2 -> filmList = itemList
                    3 -> lensList = itemList
                }
                itemText.setText("")
                currentItem = -1

            }
            alertBuilder.setNegativeButton("No, wait") { _, _ -> } // do nothing

            val warnDialog: Dialog = alertBuilder.create()
            warnDialog.show()

            true
        }

        saveButton.setOnClickListener {
            if (saveButton.text == resources.getString(R.string.save_button)) {
                (activity as MainActivity).saveAutocompleteLists(makerList, cameraList, filmList, lensList)
                dismiss()
            } else if (saveButton.text == resources.getString(R.string.update_button)) {
                itemList[currentItem] = itemText.text.toString()
                when (currentList) {
                    0 -> makerList = itemList
                    1 -> cameraList = itemList
                    2 -> filmList = itemList
                    3 -> lensList = itemList
                }
                currentItem = -1
                itemText.setText("")
                itemListAdapter.notifyDataSetChanged()
                saveButton.text = resources.getString(R.string.save_button)
            }
        }

        closeButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    private fun showHelp(context: Context) {
        // Help information for this dialog
        val infoBuilder = AlertDialog.Builder(context)
        infoBuilder.setTitle(resources.getString(R.string.autocomplete_list_edit))
        infoBuilder.setMessage(resources.getString(R.string.autocomplete_dialog_info))
        infoBuilder.setPositiveButton("Ok") { _, _ -> } // Do nothing, just disappear
        infoBuilder.create().show()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = AutocompleteDialog::class.java.simpleName
    }
}