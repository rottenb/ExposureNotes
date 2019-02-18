package com.brianmk.exposurenotes

data class CameraData(var manu: String = "",
                      var name: String = "",
                      var serial: String = "",
                      var formatIdx: Int = 0) {

    private val formats = arrayOf("35mm", "120mm")

    private var format = formats[formatIdx]

    fun updateData() {
        format = formats[formatIdx]
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}

