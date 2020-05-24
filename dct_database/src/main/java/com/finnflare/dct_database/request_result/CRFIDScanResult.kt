package com.finnflare.dct_database.request_result

data class CRFIDScanResult (
    val guid: String,
    val gtin: String,
    val sn: String,
    val rfid: String,
    val state: String,
    val qtyout: Int
)