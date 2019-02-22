package com.brianmk.exposurenotes

data class CameraData(var manu: String = "",
                      var name: String = "",
                      var serial: String = "",
                      var formatIdx: Int = 0,
                      var lensIdx: Int = 0,
                      var fixed: Boolean = false) {

    private val formats = arrayOf("35mm 3:2", "120mm 6x4.5", "120mm 6x6", "120mm 6x7", "120mm 6x8", "120mm 6x9", "120mm 6x12",
            "120mm 6x17", "120mm 6x24")

    var format = formats[formatIdx]

    fun updateData() {
        format = formats[formatIdx]
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}

