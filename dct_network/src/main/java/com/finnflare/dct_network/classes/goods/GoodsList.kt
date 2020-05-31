package com.finnflare.dct_network.classes.goods


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoodsList(
    @Json(name = "GOODS")
    val goods: List<Good>
)