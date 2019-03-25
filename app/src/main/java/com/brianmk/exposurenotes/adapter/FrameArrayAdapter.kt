package com.brianmk.exposurenotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.brianmk.exposurenotes.R
import com.brianmk.exposurenotes.data.FrameData
import java.util.*

class FrameArrayAdapter internal constructor(context: Context, frameData: List<FrameData>) : ArrayAdapter<FrameData>(context, 0, frameData) {

    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var rootView = view
        val frame = getItem(pos)

        val shutterArray = context.resources.getStringArray(R.array.shutter_speeds)
        val apertureArray = context.resources.getStringArray(R.array.apertures)

        if (view == null) {
            rootView = LayoutInflater.from(context).inflate(R.layout.item_list_frame,
                    parent, false)
        }

        // Populate the list view with information
        if (frame != null) {
            val frameNumberView = rootView?.findViewById<View>(R.id.number) as TextView

            if (frame.shutterIdx > 0 && frame.apertureIdx > 0) {
                frameNumberView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                (rootView.findViewById<View>(R.id.number) as TextView).text = String.format(Locale.getDefault(), "%02d", pos + 1)
            } else {
                frameNumberView.setTextColor(ContextCompat.getColor(context, R.color.grey))
                (rootView.findViewById<View>(R.id.number) as TextView).text = String.format(Locale.getDefault(), "%02d", pos + 1)
            }

            (rootView.findViewById<View>(R.id.shutter) as TextView).text = shutterArray[frame.shutterIdx]
            (rootView.findViewById<View>(R.id.aperture) as TextView).text = apertureArray[frame.apertureIdx]
            (rootView.findViewById<View>(R.id.notes) as TextView).text = frame.notes

        }

        return rootView!!
    }

    companion object {
        private val LOG_TAG = FrameArrayAdapter::class.java.simpleName
    }

}
