package com.finnflare.dct_network.classes.goods


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatesList(
    @Json(name = "STATES")
    val states: List<State>
)