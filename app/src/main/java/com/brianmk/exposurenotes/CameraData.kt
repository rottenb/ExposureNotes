package com.brianmk.exposurenotes

internal class CameraData {

    var camera: String? = null
    var lens: String? = null

    constructor() {
        this.camera = "Fuji GW690ii"
        this.lens = "EBC Fujinon 90mm f/3.5"
    }

    constructor(c: String, l: String) {
        this.camera = c
        this.lens = l
    }

    companion object {
        private val LOG_D = CameraData::class.java.simpleName
    }
}
