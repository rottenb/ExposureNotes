package com.brianmk.exposurenotes.dialog

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

        val manuText = rootView.findViewById<View>(R.id.manu_edit) as AutoCompleteTextView
        manuText.setText(arguments?.getString("manu"))
        val manuAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, arguments?.getStringArray("makers")!!)
        manuText.setAdapter(manuAdapter)

        val nameText = rootView.findViewById<View>(R.id.camera_edit) as AutoCompleteTextView
        nameText.setText(arguments?.getString("model"))
        val nameAdapter = ArrayAdapter<String>(rootView.context, R.layout.item_simple_list, arguments?.getStringArray("models")!!)
        nameText.setAdapter(nameAdapter)

        val formatRadio = rootView.findViewById<View>(R.id.format_radio) as RadioGroup

        when (arguments?.getString("format")) {
            "35mm" -> formatRadio.check(R.id.format_35)
            "120mm" -> formatRadio.check(R.id.format_120)
            "Other" -> formatRadio.check(R.id.format_other)
            else -> formatRadio.clearCheck()
        }

        val fixedCheckbox = rootView.findViewById<View>(R.id.fixed_checkbox) as CheckBox
        fixedCheckbox.isChecked = arguments!!.getBoolean("fixed")

        val lensSpin = rootView.findViewById<View>(R.id.lens_spinner) as Spinner
        val lensAdapter = ArrayAdapter.createFromResource(rootView.context,
                R.array.lenses, R.layout.item_spinner)
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
            var formatString = "Other"
            when (formatRadio.checkedRadioButtonId) {
                R.id.format_35 -> formatString = "35mm"
                R.id.format_120 -> formatString = "120mm"
            }

            if (manuText.text.toString() == "" || nameText.text.toString() == "") {
                Toast.makeText(rootView.context, "Make and Model required!", Toast.LENGTH_LONG).show()
            } else {
                (activity as MainActivity).setCameraData(
                        manuText.text.toString(),
                        nameText.text.toString(),
                        formatString,
                        lensSpin.selectedItemPosition,
                        fixedCheckbox.isChecked)
                dismiss()
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
        private val LOG_TAG = CameraDialog::class.java.simpleName
    }
}
