package com.brianmk.exposurenotes.data

data class FilmData(var maker: String = "",
                    var model: String = "",
                    var isoIdx: Int = 5,
                    var devIdx: Int = 3) {

    fun clearData() {
        maker = ""
        model = ""
        isoIdx = 5
        devIdx = 3
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
