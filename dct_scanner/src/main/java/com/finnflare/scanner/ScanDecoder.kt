package com.finnflare.scanner

import java.util.regex.Pattern

object ScanDecoder {
    fun decodeScanResult(scanRes: String): Triple<String, String, String>{
        var gtin = ""
        var sn = ""
        var rfid = ""

        if (Pattern.matches("^\\d{13}$", scanRes)){
            gtin = scanRes

            return Triple(gtin, sn, rfid)
        }

        if (Pattern.matches("^.?01\\d{14}21.{13}" + 29.toChar() + "?.*$", scanRes)){
            val startIndex: Int = scanRes.indexOf("01") + 2

            gtin = scanRes.substring(startIndex, startIndex + 14)
            sn = scanRes.substring(startIndex + 16, startIndex + 29)

            return Triple(gtin, sn, rfid)
        }

        if (Pattern.matches("^.{18}49BD1A$", scanRes)){
            val gtinEncoded = scanRes.substring(0, 10).toLong(16)
            gtin = (gtinEncoded.toString() + controlNumberGTIN(gtinEncoded.toString()))
            rfid = scanRes

            return Triple(gtin, sn, rfid)
        }

        if (Pattern.matches("^.{20}49BD$", scanRes)){
            gtin = scanRes.substring(0, 12).toLong(16).toString()
            sn = scanRes.substring(12, 20).toLong(16).toString()
            sn = sn.padStart(12 - sn.length, '0')
            rfid = scanRes

            return Triple(gtin, sn, rfid)
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