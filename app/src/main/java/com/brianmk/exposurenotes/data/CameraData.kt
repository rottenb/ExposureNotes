package com.brianmk.exposurenotes.data

data class CameraData(var maker: String = "",
                      var model: String = "",
                      var format: String = "",
                      var lensIdx: Int = 0 ) {

    fun clearData() {
        maker = ""
        model = ""
        format = ""
        lensIdx = 0
    }

    companion object {
        private val LOG_TAG = CameraData::class.java.simpleName
    }
}

