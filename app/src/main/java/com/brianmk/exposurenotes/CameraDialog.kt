package com.brianmk.exposurenotes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment


class CameraDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {

        val rootView = inflater.inflate(R.layout.camera_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val manuText = rootView.findViewById<View>(R.id.manu_edit) as TextView
        manuText.text = arguments!!.getString("manu")

        val nameText = rootView.findViewById<View>(R.id.camera_edit) as TextView
        nameText.text = arguments!!.getString("name")

        val formatSpin = rootView.findViewById<View>(R.id.format_spinner) as Spinner
        val formatAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.formats, R.layout.spinner_item)
        formatSpin.adapter = formatAdapter
        formatSpin.setSelection(arguments!!.getInt("format"))

        val fixedCheckbox = rootView.findViewById<View>(R.id.fixed_checkbox) as CheckBox
        fixedCheckbox.isChecked = arguments!!.getBoolean("fixed")

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lensAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.lenses, R.layout.spinner_item)
        lensSpin.adapter = lensAdapter
        lensSpin.setSelection(arguments!!.getInt("lens"))
        if (fixedCheckbox.isChecked) {
            lensSpin.visibility = View.INVISIBLE
        }

        val lensSpinMask = rootView.findViewById<View>(R.id.lens_spinner_mask) as TextView
        lensSpinMask.text = lensSpin.selectedItem.toString()
        if (fixedCheckbox.isChecked) {
            lensSpinMask.visibility = View.VISIBLE
        }

        fixedCheckbox.setOnClickListener {
            if (fixedCheckbox.isChecked) {
                lensSpin.visibility = View.INVISIBLE
                lensSpinMask.text = lensSpin.selectedItem.toString()
                lensSpinMask.visibility = View.VISIBLE
            } else {
                lensSpin.visibility = View.VISIBLE
                lensSpinMask.visibility = View.INVISIBLE
            }
        }

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            // This gets called if the user wants to truncate the film list
            (activity as MainActivity).setCameraData(
                    manuText.text.toString(),
                    nameText.text.toString(),
                    formatSpin.selectedItemPosition,
                    lensSpin.selectedItemPosition,
                    fixedCheckbox.isChecked)
            dismiss()
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
        private val LOG_TAG = CameraDialog::class.java.simpleName
    }
}
