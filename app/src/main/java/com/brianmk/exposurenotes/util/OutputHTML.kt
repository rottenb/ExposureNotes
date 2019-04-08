package com.brianmk.exposurenotes.util

import com.brianmk.exposurenotes.data.FrameData

class OutputHTML(camera: String, film: String, iso: String, dev: String,
                shutters: Array<String>, apertures: Array<String>,
                frameList: List<FrameData>) {

    override fun toString() : String {
        var outputString: String = "$LOG_TAG: Nothing here yet."

        return outputString
    }

    companion object {
        private val LOG_TAG = OutputHTML::class.java.simpleName
    }
}