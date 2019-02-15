package com.brianmk.exposurenotes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

class FrameArrayAdapter internal constructor(context: Context, frameData: List<FrameData>) : ArrayAdapter<FrameData>(context, 0, frameData) {

    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val frame = getItem(pos)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.frame_list_item,
                    parent, false)
        }

        // Populate the list view with information
        if (frame != null) {
            val frameNumberView = view!!.findViewById<TextView>(R.id.number)
            if (frame.shutterIdx > 0 && frame.apertureIdx > 0) {
                frameNumberView.setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
                (view.findViewById<View>(R.id.number) as TextView).text = String.format(Locale.getDefault(), "%d", pos + 1)
            } else {
                frameNumberView.setTextColor(ContextCompat.getColor(context, R.color.lighter_grey))

                (view.findViewById<View>(R.id.number) as TextView).text = String.format(Locale.getDefault(), "%d", pos + 1)
            }

            (view.findViewById<View>(R.id.shutter) as TextView).text = frame.shutter
            (view.findViewById<View>(R.id.aperture) as TextView).text = frame.aperture
            (view.findViewById<View>(R.id.notes) as TextView).text = frame.notes


        } else {
            Log.d(LOG_TAG, "frame #$pos is null.")
        }

        return view!!
    }

    companion object {
        private val LOG_TAG = FrameArrayAdapter::class.java.simpleName
    }

}
