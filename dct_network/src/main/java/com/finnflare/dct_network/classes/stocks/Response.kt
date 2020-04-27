package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val error: Boolean,
    val goods: Goods,
    val leftovers: Leftovers,
    @Json(name = "marking_codes")
    val markingCodes: MarkingCodes,
    val message: String,
    val states: States,
    val status: String
)