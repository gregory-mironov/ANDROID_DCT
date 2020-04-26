package com.finnflare.dct_network.dataclasses.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Leftover(
    @Json(name = "_DOC_GUID")
    val docGuid: String,
    @Json(name = "_DOC_NUMBER")
    val docNumber: String,
    @Json(name = "_GTIN")
    val gtin: String,
    @Json(name = "_GUID")
    val guid: String,
    @Json(name = "_QTYIN")
    val qtyin: Int,
    @Json(name = "_RFID")
    val rfid: Any,
    @Json(name = "_SN")
    val sn: Any,
    @Json(name = "_STATE")
    val state: String,
    @Json(name = "_STORE_GUID")
    val storeGuid: String
)