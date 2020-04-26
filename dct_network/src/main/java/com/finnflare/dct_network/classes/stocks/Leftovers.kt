package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Leftovers(
    @Json(name = "LEFTOVERS")
    val leftovers: List<Leftover>
)