package com.finnflare.dct_network.dataclasses.stores


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Header(
    val method: String,
    val token: String
)