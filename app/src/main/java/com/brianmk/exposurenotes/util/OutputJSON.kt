package com.brianmk.exposurenotes.util

import android.util.Log
import com.brianmk.exposurenotes.data.CameraData
import com.brianmk.exposurenotes.data.FilmData
import com.brianmk.exposurenotes.data.FrameData

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class OutputJSON : JSONObject() {

    fun createJSONobj(c: CameraData, f: FilmData, i: String, e: String, s: Array<String>, a: Array<String>, frames: List<FrameData>): JSONObject {

        val obj = JSONObject()
        val filmArray = JSONArray()
        for (i in frames.indices) {
            val frameArray = JSONArray()

            frameArray.put(s[frames[i].shutterIdx])
            frameArray.put(a[frames[i].apertureIdx])
            frameArray.put(frames[i].notes)

            filmArray.put(frameArray)
        }

        val cal = Calendar.getInstance()
        val date = ("${cal.get(Calendar.DAY_OF_MONTH)} ${cal.get(Calendar.MONTH)} ${cal.get(Calendar.YEAR)}\n")

        try {
            obj.put("date", date)
            obj.put("camera", c.maker + c.model)
            obj.put("film", f.maker + f.model)
            obj.put("iso", i)
            obj.put("exp", e)
            obj.put("frames", filmArray)
        } catch (e: JSONException) {
            Log.d(LOG_TAG, "JSON exception!!")
            e.printStackTrace()
        }

        return obj
    }

    companion object {
        private val LOG_TAG = OutputJSON::class.java.simpleName
    }
}
