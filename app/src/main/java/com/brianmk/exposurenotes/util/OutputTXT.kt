package com.brianmk.exposurenotes.util

import com.brianmk.exposurenotes.data.CameraData
import com.brianmk.exposurenotes.data.FilmData
import com.brianmk.exposurenotes.data.FrameData
import java.util.*

class OutputTXT(c: CameraData, f: FilmData, i: String, e: String, s: Array<String>, a: Array<String>, frames: List<FrameData>) {
    private val camera = c.maker + " " + c.model
    private val film = f.maker + " " + f.model
    private val iso = i
    private val exp = e
    private val shutters = s
    private val apertures = a
    private val framesList = frames

    override fun toString() : String {
        val out = StringBuilder()
        val cal = Calendar.getInstance()
        var month = ""
        when (cal.get(Calendar.MONTH)) {
            1 -> month = "JAN"
            2 -> month = "FEB"
            3 -> month = "MAR"
            4 -> month = "APR"
            5 -> month = "MAY"
            6 -> month = "JUN"
            7 -> month = "JUL"
            8 -> month = "AUG"
            9 -> month = "SEP"
            10 -> month = "OCT"
            11 -> month = "NOV"
            12 -> month = "DEC"
        }



        val date = "${cal.get(Calendar.DAY_OF_MONTH)} $month ${cal.get(Calendar.YEAR)}"
        val time = "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"

        out.append("EXPORTED: $date @ $time \n")
        out.append("CAMERA: $camera\nFILM: $film\nISO: $iso\nEXP.COMP: $exp\n\n")
        for (i in 0 until framesList.size) {
            val n = String.format(Locale.getDefault(), "%02d", i + 1)
            out.append("$n  ${shutters[framesList[i].shutterIdx]}\t${apertures[framesList[i].apertureIdx]}\t${framesList[i].notes}\t${framesList[i].focalLength}mm\t${framesList[i].lens}\n")
        }

        return out.toString()
    }

    companion object {
        private val LOG_TAG = OutputTXT::class.java.simpleName
    }
}