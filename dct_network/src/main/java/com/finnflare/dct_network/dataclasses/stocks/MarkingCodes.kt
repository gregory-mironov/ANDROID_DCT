package com.finnflare.dct_network.dataclasses.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkingCodes(
    @Json(name = "MARKING_CODES")
    val markingCodes: List<MarkingCode>
)