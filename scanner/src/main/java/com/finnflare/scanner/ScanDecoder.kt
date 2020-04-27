package com.finnflare.scanner

import java.util.regex.Pattern

object ScanDecoder {
    fun decodeScanResult(scanRes: String): Triple<String, String, String>{
        var gtin = ""
        var sn = ""
        var rfid = ""

        if (Pattern.matches("^\\d{13}$", scanRes)){
            gtin = scanRes
        }

        if (Pattern.matches("^.?01\\d{14}21.{13}", scanRes)){
            val startIndex: Int = scanRes.indexOf("01") + 2

            gtin = scanRes.substring(startIndex, startIndex + 14)
            sn = scanRes.substring(startIndex + 16, startIndex + 29)
        }

        if (Pattern.matches("^.{18}49BD1A$", scanRes) && scanRes.endsWith("48BD1A") ){
            val gtinEncoded = scanRes.substring(0, 10).toLong(16)
            gtin = (gtinEncoded.toString() + controlNumberGTIN(gtinEncoded.toString()))
            rfid = scanRes
        }

        if (Pattern.matches("^.{20}49BD$", scanRes) && scanRes.endsWith("48BD1A") ){
            gtin = scanRes.substring(0, 12).toLong(16).toString()
            sn = scanRes.substring(12, 20)
            rfid = scanRes
        }

        return Triple(gtin, sn, rfid)
    }

    private fun controlNumberGTIN(str: String): String? {
        var ch = 0
        var nch = 0
        for (i in str.indices step 2)
            ch += Character.digit(str[i], 10)

        for (i in 1 until str.length step 2)
            nch += Character.digit(str[i], 10)

        return ((10 - (ch + 3 * nch) % 10) % 10).toString()
    }
}