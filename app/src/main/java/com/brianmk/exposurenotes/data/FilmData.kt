package com.brianmk.exposurenotes.data

data class FilmData(var manu: String = "",
                    var name: String = "",
                    var isoIdx: Int = 5,
                    var frames: Int = 0,
                    var devIdx: Int = 4,
                    var notes: String = "") {

    private val isos = arrayOf("0", "5", "12", "25", "50", "100", "160", "200", "400", "800", "1600", "3200", "6400")
    private val devs = arrayOf("0", "-3", "-2", "-1", "0", "+1", "+2", "+3")

    var iso = isos[isoIdx]
    var dev = devs[devIdx]

    fun updateData() {
        iso = isos[isoIdx]
        dev = devs[devIdx]
    }

    fun clearData() {
        manu = ""
        name = ""
        isoIdx = 5
        frames = 0
        devIdx = 4
        notes = ""

        updateData()
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
