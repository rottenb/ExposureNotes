package com.brianmk.exposurenotes

data class FilmData(var manu: String = "Ilford",
                    var name: String = "HP5+",
                    var formatIdx: Int = 1,
                    var isoIdx: Int = 5,
                    var frames: Int = 12,
                    var devIdx: Int = 4,
                    var notes: String = "") {

    private val isos = arrayOf("", "5", "12", "25", "50", "100", "160", "200", "400", "800", "1600", "3200", "6400")
    private val devs = arrayOf("±X", "-3", "-2", "-1", "0", "+1", "+2", "+3")
    private val formats = arrayOf("", "35mm", "120mm")


    var iso = isos[isoIdx]
    var dev = devs[devIdx]
    var format = formats[formatIdx]

    fun updateSettings() {
        iso = isos[isoIdx]
        dev = devs[devIdx]
        format = formats[formatIdx]
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
