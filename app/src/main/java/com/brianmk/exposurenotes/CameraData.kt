package com.brianmk.exposurenotes

data class CameraData(var manu: String = "Canon",
                      var name: String = "FTb QL",
                      var serial: String = "752235",
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

