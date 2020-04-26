package com.finnflare.dct_network.classes.auth


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val error: Boolean,
    val message: String,
    val status: String
)