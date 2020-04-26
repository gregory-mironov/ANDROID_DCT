package com.finnflare.dct_network.dataclasses.docs


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val data: List<Data>,
    val error: Boolean,
    val message: String,
    val status: String
)