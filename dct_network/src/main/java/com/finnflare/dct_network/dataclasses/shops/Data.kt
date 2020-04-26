package com.finnflare.dct_network.dataclasses.shops


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "HttpRef")
    val httpRef: String,
    @Json(name = "Id")
    val id: String,
    @Json(name = "Name")
    val name: String
)