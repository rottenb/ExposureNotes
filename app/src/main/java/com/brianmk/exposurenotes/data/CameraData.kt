package com.brianmk.exposurenotes.data

data class CameraData(var manu: String = "",
                      var name: String = "",
                      var format: String = "Other",
                      var lensIdx: Int = 0,
                      var fixed: Boolean = false) {

    fun clearData() {
        manu = ""
        name = ""
        format = "Other"
        lensIdx = 0
        fixed = false
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}

