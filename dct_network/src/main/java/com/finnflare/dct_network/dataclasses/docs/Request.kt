package com.finnflare.dct_network.dataclasses.docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "DocDate")
    val docDate: String,
    @Json(name = "ShopId")
    val shopId: String
)