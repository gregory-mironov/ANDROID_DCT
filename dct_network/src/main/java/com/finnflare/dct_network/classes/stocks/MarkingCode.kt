package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkingCode(
    @Json(name = "_GTIN")
    val gtin: String,
    @Json(name = "_GUID")
    val guid: String,
    @Json(name = "_RFID")
    val rfid: Any,
    @Json(name = "_SN")
    val sd: Any,
    @Json(name = "_STATE")
    val state: String
)