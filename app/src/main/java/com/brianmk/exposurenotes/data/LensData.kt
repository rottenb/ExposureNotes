package com.brianmk.exposurenotes.data

data class LensData(var maker: String = "",
                    var model: String = "",
                    var minZoom: Int = 0,
                    var maxZoom: Int = 0,
                    var extras: String = "",
                    var prime: Boolean = true,
                    var fixed: Boolean = false) {


    companion object {
        private val LOG_TAG = LensData::class.java.simpleName
    }
}

