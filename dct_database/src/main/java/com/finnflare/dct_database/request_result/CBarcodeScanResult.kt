package com.finnflare.dct_database.request_result

data class CBarcodeScanResult (
    val guid: String,
    val gtin: String,
    val sn: String,
    val state: String,
    val qtyout: Int
)