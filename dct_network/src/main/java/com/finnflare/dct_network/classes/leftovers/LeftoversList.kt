package com.finnflare.dct_network.classes.leftovers


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeftoversList(
    @Json(name = "LEFTOVERS")
    val leftovers: List<Leftover>
)