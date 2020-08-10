package com.finnflare.dct_network.classes.marking_codes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val error: Boolean,
    @Json(name = "marking_codes")
    val markingCodes: MarkingCodesList?,
    val message: String,
    val status: String
)