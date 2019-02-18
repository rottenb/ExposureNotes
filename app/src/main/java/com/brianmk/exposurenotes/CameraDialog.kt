package com.brianmk.exposurenotes

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment


class CameraDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.camera_dialog, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        val manuText = rootView.findViewById<View>(R.id.manu_edit) as TextView
        manuText.text = arguments!!.getString("manu")

        val nameText = rootView.findViewById<View>(R.id.camera_edit) as TextView
        nameText.text = arguments!!.getString("name")

        val saveButton = rootView.findViewById<View>(R.id.save_button) as Button
        saveButton.setOnClickListener {
            (activity as MainActivity).setCameraData(
                    manuText.text.toString(),
                    nameText.text.toString())
            dismiss()
        }

        val cancelButton = rootView.findViewById<View>(R.id.cancel_button) as Button
        cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }
    companion object {
        private val LOG_TAG = CameraDialog::class.java.simpleName
    }
}
