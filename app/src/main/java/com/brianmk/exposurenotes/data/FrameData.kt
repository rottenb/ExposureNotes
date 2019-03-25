package com.brianmk.exposurenotes.data

data class FrameData(var shutterIdx: Int = 0,
                     var apertureIdx: Int = 0,
                     var lens: String = "",
                     var notes: String = "") {

    companion object {
        private val LOG_TAG = FrameData::class.java.simpleName
    }
}
