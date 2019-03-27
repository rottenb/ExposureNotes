package com.brianmk.exposurenotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.brianmk.exposurenotes.R

class LensArrayAdapter internal constructor(context: Context, lensList: MutableList<String>) : ArrayAdapter<String>(context, 0, lensList) {

    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var rootView = view
        val lens = getItem(pos)

        if (view == null) {
            rootView = LayoutInflater.from(context).inflate(R.layout.item_lens_list,
                    parent, false)
        }

        val lensText = rootView?.findViewById<View>(R.id.lens_text) as TextView
        if (lens != null) {
            if (pos == 0) {
                lensText.text = rootView.resources.getString(R.string.add_new)
                lensText.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                lensText.text = lens
                lensText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            }
        }

        return rootView
    }




    companion object {
        private val LOG_TAG = LensArrayAdapter::class.java.simpleName
    }

}
