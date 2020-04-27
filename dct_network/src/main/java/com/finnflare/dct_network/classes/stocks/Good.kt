package com.finnflare.dct_network.classes.stocks


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Good(
    @Json(name = "_COLOR")
    val color: String,
    @Json(name = "_GUID")
    val guid: String,
    @Json(name = "_MODEL")
    val model: String,
    @Json(name = "_NAME")
    val name: String,
    @Json(name = "_SIZE")
    val size: String
)