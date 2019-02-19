package com.brianmk.exposurenotes

data class FilmData(var manu: String = "",
                    var name: String = "",
                    var formatIdx: Int = 0,
                    var isoIdx: Int = 0,
                    var frames: Int = 0,
                    var devIdx: Int = 0,
                    var notes: String = "") {

    private val formats = arrayOf("35mm", "35mm", "120mm 6x4.5", "120mm 6x4.5", "120mm 6x6",
                                    "120mm 6x6", "120mm 6x7", "120mm 6x8", "120mm 6x9", "120mm 6x12",
                                    "120mm 6x17", "120mm 6x24")

    private val isos = arrayOf("0", "5", "12", "25", "50", "100", "160", "200", "400", "800", "1600", "3200", "6400")
    private val devs = arrayOf("0", "-3", "-2", "-1", "0", "+1", "+2", "+3")

    var format = formats[formatIdx]
    var iso = isos[isoIdx]
    var dev = devs[devIdx]

    fun updateData() {
        format = formats[formatIdx]
        iso = isos[isoIdx]
        dev = devs[devIdx]
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
