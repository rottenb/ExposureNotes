package com.brianmk.exposurenotes.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.brianmk.exposurenotes.R

class AboutDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_about, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)
/*
        val scriptButton = rootView.findViewById<View>(R.id.script_button) as Button
        scriptButton.setOnClickListener {
            // TODO Dump a python script
            dismiss()
        }
*/
        (rootView.findViewById<View>(R.id.close_button) as Button).setOnClickListener {
            dismiss()
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = AboutDialog::class.java.simpleName
    }
}