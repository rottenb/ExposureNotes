package com.brianmk.exposurenotes.data

data class FilmData(var maker: String = "",
                    var model: String = "",
                    var isoIdx: Int = 5,
                    var frames: Int = 0,
                    var devIdx: Int = 3 ) {

    fun clearData() {
        maker = ""
        model = ""
        isoIdx = 5
        frames = 0
        devIdx = 3
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
