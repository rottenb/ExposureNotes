package com.brianmk.exposurenotes

class FilmData {


    var film: String? = null
    var iso: String? = null
    var dev: String? = null

    constructor() {
        this.film = ""
        this.iso = ""
        this.dev = "0"
    }


    constructor(c: String, l: String, f: String, i: String, d: String) {
        this.film = f
        this.iso = i
        this.dev = d
    }

    companion object {
        private val LOG_TAG = FilmData::class.java.simpleName
    }
}
