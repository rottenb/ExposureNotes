package com.brianmk.exposurenotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
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

        lensText.text = lens

        return rootView
    }




    companion object {
        private val LOG_TAG = LensArrayAdapter::class.java.simpleName
    }

}
