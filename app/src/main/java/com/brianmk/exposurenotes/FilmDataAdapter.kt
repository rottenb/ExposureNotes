package com.brianmk.exposurenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FilmDataAdapter internal constructor(c: Context, filmData: List<FilmData>) : ArrayAdapter<FilmData>(c, 0, filmData) {

    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var view = view

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_main,
                    parent, false)
        }

        val camera = view!!.findViewById<TextView>(R.id.camera_type)


        return view
    }

    companion object {
        private val LOG_TAG = FilmDataAdapter::class.java.simpleName
    }


}
