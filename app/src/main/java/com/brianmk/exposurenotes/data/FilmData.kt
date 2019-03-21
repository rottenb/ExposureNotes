package com.brianmk.exposurenotes.data

data class FilmData(var maker: String = "",
                    var model: String = "",
                    var isoIdx: Int = 5,
                    var frames: Int = 0,
                    var devIdx: Int = 3 ) {

    // ISOs in 1/3rd stop increments
    private val isos = arrayOf("0", "5", "12", "25", "50", "100", "125", "160", "200", "250", "320", "400", "500", "640", "800", "1000", "1250", "1600", "2000", "2500", "3200", "4000", "5000", "6400")
    private val devs = arrayOf("-3", "-2", "-1", "±0", "+1", "+2", "+3")

    var iso = isos[isoIdx]
    var dev = devs[devIdx]

    fun updateData() {
        iso = isos[isoIdx]
        dev = devs[devIdx]
    }

    fun clearData() {
        maker = ""
        model = ""
        isoIdx = 5
        frames = 0
        devIdx = 3

        updateData()
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
