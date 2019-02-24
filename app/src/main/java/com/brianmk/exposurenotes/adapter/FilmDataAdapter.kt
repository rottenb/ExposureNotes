package com.brianmk.exposurenotes.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.brianmk.exposurenotes.data.FilmData

class FilmDataAdapter internal constructor(c: Context, filmData: List<FilmData>) : ArrayAdapter<FilmData>(c, 0, filmData) {
/*
    override fun getView(pos: Int, view: View?, parent: ViewGroup): View {
        var view = view

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_main,
                    parent, false)
        }

        val camera = view!!.findViewById<TextView>(R.id.camera_name)


        return view
    }
*/
    companion object {
        private val LOG_TAG = FilmDataAdapter::class.java.simpleName
    }


}
