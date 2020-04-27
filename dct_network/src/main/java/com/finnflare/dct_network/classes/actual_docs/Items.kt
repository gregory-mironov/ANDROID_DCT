package com.finnflare.dct_network.classes.actual_docs


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Items(
    val guid: String,
    val qtyin: Int,
    val qtyout: Int,
    val rfid: String,
    val sn: String,
    val state: String
)