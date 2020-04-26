package com.finnflare.dct_network.dataclasses.actual_docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Header(
    val method: String,
    val token: String
)