package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Goods(
    @Json(name = "GOODS")
    val Goods: List<Good>
)