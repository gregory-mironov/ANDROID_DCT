package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "DocID")
    val docID: String
)