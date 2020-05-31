package com.finnflare.dct_network.classes.marking_codes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkingCodesList(
    @Json(name = "MARKING_CODES")
    val markingCodes: List<MarkingCode>
)