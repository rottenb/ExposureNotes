package com.brianmk.exposurenotes.util

import android.util.Log
import com.brianmk.exposurenotes.data.FrameData

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class OutputJSON : JSONObject() {

    fun createJSONobj(camera: String, film: String, iso: String, dev: String,
                      frameList: List<FrameData>): JSONObject {

        val obj = JSONObject()
        val filmArray = JSONArray()
        for (i in frameList.indices) {
            val frameArray = JSONArray()

            frameArray.put(frameList[i].shutter)
            frameArray.put(frameList[i].aperture)
            frameArray.put(frameList[i].notes)

            filmArray.put(frameArray)
        }

        try {
            obj.put("camera", camera)
            obj.put("film", film)
            obj.put("iso", iso)
            obj.put("dev", dev)
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
