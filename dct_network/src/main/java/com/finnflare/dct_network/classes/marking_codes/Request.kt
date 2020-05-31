package com.finnflare.dct_network.classes.marking_codes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    val limit: Int,
    val page: Int,
    @Json(name = "ShopID")
    val shopId: String
)