package com.brianmk.exposurenotes.data

data class FrameData(var shutterIdx: Int = 0, var apertureIdx: Int = 0,
                     var lensIdx: Int = 0, var notes: String = "") {

    private val shutters = arrayOf("", "1/500s", "1/250s", "1/125s", "1/60s", "1/30s",
                                    "1/15s", "1/8s", "1/4s", "1/2s", "1s", "T", "B")

    private val apertures = arrayOf("", "f/1.0", "f/1.2", "f/1.4", "f/1.8", "f/2.0", "f/2.8",
                                    "f/3.5", "f/4.0", "f/4.8", "f/5.6", "f/6.7", "f/8.0",
                                    "f/9.5", "f/11.0", "f/13.0", "f/16.0", "f/19.0", "f/22.0",
                                    "f/27.0", "f/32.0")

    var shutter: String = shutters[shutterIdx]
    var aperture: String = apertures[apertureIdx]

    fun updateData() {
        shutter = shutters[shutterIdx]
        aperture = apertures[apertureIdx]
    }

    companion object {
        private val LOG_TAG = FrameData::class.java.simpleName
    }
}
