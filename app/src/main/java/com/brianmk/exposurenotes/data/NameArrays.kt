package com.brianmk.exposurenotes.data

data class NameArrays(var cameraMakers: MutableList<String> = mutableListOf("Canon", "Fujifilm", "Google", "Mamiya"),
                      var cameraModels: MutableList<String> = mutableListOf("FTb QL", "X-T1", "Pixel 2 XL", "RB67sd", "Six Automat II"),

                      var filmMakers: MutableList<String> = mutableListOf("Fujifilm", "Ilford", "Kodak", "JCH"),
                      var filmModels: MutableList<String> = mutableListOf("Pro400H", "HP5+", "FP4+", "Delta 100", "Tri-x", "Portra"),

                      var lensMakers: MutableList<String> = mutableListOf("Canon", "Olympus"),
                      var lensModels: MutableList<String> = mutableListOf("50mm f/1.8", "300mm f/4.0")) {

    override fun equals(other: Any?) : Boolean {
        return super.equals(other)
    }

    override fun hashCode() : Int {
        return super.hashCode()
    }
}