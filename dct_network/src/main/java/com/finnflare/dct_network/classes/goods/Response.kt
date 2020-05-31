package com.finnflare.dct_network.classes.goods


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response(
    val error: Boolean,
    val goods: GoodsList?,
    val message: String,
    val states: StatesList,
    val status: String
)