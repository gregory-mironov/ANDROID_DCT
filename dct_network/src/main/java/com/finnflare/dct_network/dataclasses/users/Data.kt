package com.finnflare.dct_network.dataclasses.users


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "Id")
    val id: String,
    @Json(name = "Name")
    val name: String
)