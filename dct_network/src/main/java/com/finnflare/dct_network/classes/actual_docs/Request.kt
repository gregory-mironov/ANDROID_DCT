package com.finnflare.dct_network.classes.actual_docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "Docs")
    val docs: List<Doc>
)