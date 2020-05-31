package com.finnflare.dct_network.classes.goods


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class State(
    @Json(name = "_STATE")
    val state: String,
    @Json(name = "_STATE_NAME")
    val stateName: String
)