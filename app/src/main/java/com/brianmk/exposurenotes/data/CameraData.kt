package com.brianmk.exposurenotes.data

data class CameraData(var maker: String = "",
                      var model: String = "",
                      var formatIdx: Int = 0,
                      var lens: String = "" ) {

    fun clearData() {
        maker = ""
        model = ""
        formatIdx = 0
        lens = ""
    }

    companion object {
        private val LOG_TAG = CameraData::class.java.simpleName
    }
}

