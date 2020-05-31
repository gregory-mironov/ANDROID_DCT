package com.finnflare.dct_network.classes.leftovers


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val error: Boolean,
    val leftovers: LeftoversList?,
    val message: String,
    val status: String
)