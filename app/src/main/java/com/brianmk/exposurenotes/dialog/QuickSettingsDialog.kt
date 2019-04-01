package com.brianmk.exposurenotes.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brianmk.exposurenotes.R

class QuickSettingsDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val rootView = inflater.inflate(R.layout.dialog_quick_settings, container)
        rootView.setBackgroundColor(Color.TRANSPARENT)

        return rootView
    }

    override fun onResume() {
        super.onResume()

        dialog?.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private val LOG_TAG = QuickSettingsDialog::class.java.simpleName
    }
}