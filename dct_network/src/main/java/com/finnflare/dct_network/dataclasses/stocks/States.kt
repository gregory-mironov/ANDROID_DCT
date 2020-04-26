package com.finnflare.dct_network.dataclasses.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class States(
    @Json(name = "STATES")
    val states: List<State>
)