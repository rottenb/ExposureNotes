package com.brianmk.exposurenotes.data

data class ProductNames(var makers: MutableList<String> = mutableListOf("Canon", "Fujifilm", "Google", "Mamiya", "Nikon", "Ilford", "Olympus", "Fujinon", "Nikkor", "Kodak", "JCH", "Seiko"),
                        var cameraModels: MutableList<String> = mutableListOf("FTb QL", "X-T1", "Pixel 2 XL", "RB67sd", "Six Automat II"),
                        var lensModels: MutableList<String> = mutableListOf(" Unknown ", "Canon 50mm f/1.8", "Canon 300mm f/4.0", "Mamiya 90mm f/3.5", "Olympus D.Zuiko FC 70mm f/3.5"),
                        var filmModels: MutableList<String> = mutableListOf("Pro400H", "HP5+", "FP4+", "Delta 100", "Tri-x", "Portra")) {

    override fun equals(other: Any?) : Boolean {
        return super.equals(other)
    }

    override fun hashCode() : Int {
        return super.hashCode()
    }
}