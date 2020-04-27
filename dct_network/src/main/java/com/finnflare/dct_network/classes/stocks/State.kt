package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class State(
    @Json(name = "_STATE")
    val sTATE: String,
    @Json(name = "_STATE_NAME")
    val sTATENAME: String
)