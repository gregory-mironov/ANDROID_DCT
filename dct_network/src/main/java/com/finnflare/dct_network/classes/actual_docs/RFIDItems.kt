package com.finnflare.dct_network.classes.actual_docs


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RFIDItems(
    val guid: String,
    val gtin: String,
    val sn: String,
    val rfid: String,
    val state: String,
    val qtyout: Int
)